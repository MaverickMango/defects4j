package org.mockito.internal.verification;


public class VerificationOverTimeImpl implements org.mockito.verification.VerificationMode {
    private final long pollingPeriodMillis;

    private final long durationMillis;

    private final org.mockito.verification.VerificationMode delegate;

    private final boolean returnOnSuccess;

    private final org.mockito.internal.util.Timer timer;

    public VerificationOverTimeImpl(long pollingPeriodMillis, long durationMillis, org.mockito.verification.VerificationMode delegate, boolean returnOnSuccess) {
        this(pollingPeriodMillis, durationMillis, delegate, returnOnSuccess, new org.mockito.internal.util.Timer(durationMillis));
    }

    public VerificationOverTimeImpl(long pollingPeriodMillis, long durationMillis, org.mockito.verification.VerificationMode delegate, boolean returnOnSuccess, org.mockito.internal.util.Timer timer) {
        this.pollingPeriodMillis = pollingPeriodMillis;
        this.durationMillis = durationMillis;
        this.delegate = delegate;
        this.returnOnSuccess = returnOnSuccess;
        this.timer = timer;
    }

    public void verify(org.mockito.internal.verification.api.VerificationData data) {
        java.lang.AssertionError error = null;
        timer.start();
        while (timer.isCounting()) {
            try {
                delegate.verify(data);
                if (returnOnSuccess) {
                    return;
                }else {
                    error = null;
                }
            } catch (org.mockito.exceptions.base.MockitoAssertionError e) {
                error = handleVerifyException(e);
            } catch (java.lang.AssertionError e) {
                error = handleVerifyException(e);
            }
        } 
        if (error != null) {
            throw error;
        }
    }

    private java.lang.AssertionError handleVerifyException(java.lang.AssertionError e) {
        if (canRecoverFromFailure(delegate)) {
            sleep(pollingPeriodMillis);
            return e;
        }else {
            throw e;
        }
    }

    protected boolean canRecoverFromFailure(org.mockito.verification.VerificationMode verificationMode) {
        return !((verificationMode instanceof org.mockito.internal.verification.AtMost) || (verificationMode instanceof org.mockito.internal.verification.NoMoreInteractions));
    }

    private void sleep(long sleep) {
        try {
            java.lang.Thread.sleep(sleep);
        } catch (java.lang.InterruptedException ie) {
        }
    }

    public long getPollingPeriod() {
        return pollingPeriodMillis;
    }

    public long getDuration() {
        return durationMillis;
    }

    public org.mockito.verification.VerificationMode getDelegate() {
        return delegate;
    }
}

