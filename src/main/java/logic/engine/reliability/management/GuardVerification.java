package logic.engine.reliability.management;

import logic.engine.user.User;

public class GuardVerification {
    User guard;
    Verification verification;

    public GuardVerification(User guard, Verification verification) {
        this.guard = guard;
        this.verification = verification;
    }

    public User getGuard() {
        return guard;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }
}
