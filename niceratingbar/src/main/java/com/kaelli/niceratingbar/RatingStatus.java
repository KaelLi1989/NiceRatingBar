package com.kaelli.niceratingbar;

/**
 * Created by KaelLi on 2019/11/15.
 */
public enum RatingStatus {
    Disable(0),
    Enable(1);

    int mStatus;

    RatingStatus(int statusValue) {
        this.mStatus = statusValue;
    }

    public static RatingStatus getStatus(int status) {
        return status == Disable.mStatus ? Disable : Enable;
    }
}
