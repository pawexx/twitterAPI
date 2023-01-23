package com.twitter.dto.controller;

import java.time.LocalDateTime;

public record Tweet(Long id, LocalDateTime createdAt, String text) {}