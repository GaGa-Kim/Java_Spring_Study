package com.gaga.springtoby.user.service;

import com.gaga.springtoby.user.domain.User;

/**
 * 업그레이드 정책 인터페이스
 */
public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
