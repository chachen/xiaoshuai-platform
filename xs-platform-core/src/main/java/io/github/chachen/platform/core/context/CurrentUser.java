package io.github.chachen.platform.core.context;

public record CurrentUser(Long id, String username, String displayName) {
    private static final ThreadLocal<CurrentUser> HOLDER = new ThreadLocal<>();

    public static void set(CurrentUser user) {
        HOLDER.set(user);
    }

    public static CurrentUser get() {
        return HOLDER.get();
    }

    public static Long currentId() {
        return HOLDER.get() == null ? null : HOLDER.get().id();
    }

    public static boolean isAuthenticated() {
        return HOLDER.get() != null;
    }

    public static void clear() {
        HOLDER.remove();
    }
}
