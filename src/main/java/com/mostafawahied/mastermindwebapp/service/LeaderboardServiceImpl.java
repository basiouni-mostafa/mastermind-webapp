package com.mostafawahied.mastermindwebapp.service;

import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;

import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService {
    private UserRepository userRepository;
    @Override
    public List<User> getTopTenUsers() {
        return userRepository.findByOrderByScoreDesc();
    }
}
