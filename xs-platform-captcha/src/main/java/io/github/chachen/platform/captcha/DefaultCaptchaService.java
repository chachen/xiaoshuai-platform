package io.github.chachen.platform.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

public class DefaultCaptchaService implements CaptchaService {
    private static final char[] CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    private final CaptchaStore store;
    private final int expireSeconds;
    private final boolean ignoreCase;
    private final SecureRandom random = new SecureRandom();

    public DefaultCaptchaService(CaptchaStore store, int expireSeconds, boolean ignoreCase) {
        this.store = store;
        this.expireSeconds = expireSeconds;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public CaptchaResult generate() {
        String answer = randomText(4), key = UUID.randomUUID().toString();
        store.save(key, answer, Duration.ofSeconds(expireSeconds));
        try {
            BufferedImage image = new BufferedImage(120, 42, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 120, 42);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            for (int i = 0; i < answer.length(); i++) {
                g.setColor(new Color(random.nextInt(130), random.nextInt(130), random.nextInt(130)));
                g.drawString(String.valueOf(answer.charAt(i)), 13 + i * 25, 29);
            }
            g.setColor(new Color(210, 210, 210));
            for (int i = 0; i < 5; i++)
                g.drawLine(random.nextInt(120), random.nextInt(42), random.nextInt(120), random.nextInt(42));
            g.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return new CaptchaResult(key, "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray()), expireSeconds);
        } catch (Exception e) {
            throw new IllegalStateException("验证码图片生成失败", e);
        }
    }

    private String randomText(int length) {
        StringBuilder b = new StringBuilder(length);
        for (int i = 0; i < length; i++) b.append(CHARS[random.nextInt(CHARS.length)]);
        return b.toString();
    }

    @Override
    public void verify(String key, String answer) {
        String expected = key == null ? null : store.get(key);
        store.delete(key);
        if (expected == null || answer == null || !(ignoreCase ? expected.equalsIgnoreCase(answer) : expected.equals(answer)))
            throw new CaptchaException("CAPTCHA_INVALID", "验证码错误或已过期");
    }
}
