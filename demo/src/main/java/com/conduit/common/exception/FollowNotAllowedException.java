package com.conduit.common.exception;

public class FollowNotAllowedException extends RuntimeException {
    public FollowNotAllowedException(String message) {
        super("you cannot follow or unfollow " + message);
    }
}
