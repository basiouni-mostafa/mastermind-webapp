package com.mostafawahied.mastermindwebapp.service;

import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
    private final UserRepository userRepository;

    @Autowired
    public LeaderboardServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable("leaderboard")
    public List<User> getTopTenUsers() {
        return userRepository.findByOrderByScoreDesc();
    }
}
