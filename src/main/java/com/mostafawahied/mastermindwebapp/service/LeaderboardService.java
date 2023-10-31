package com.mostafawahied.mastermindwebapp.service;

import com.mostafawahied.mastermindwebapp.model.User;

import java.util.List;

public interface LeaderboardService {
    List<User> getTopTenUsers();
}
