package com.disasterrelief.util;

import com.disasterrelief.model.ResourceRequest;

import java.sql.Timestamp;

/**
 * Algorithmic Priority Score Calculator for Resource Allocation.
 * Uses a multi-factor formula:
 * Score = (UrgencyWeight * 40) + (PeopleAffected * 0.35) + (WaitingHours * 2.0) + DisasterSeverityBonus
 */
public class PriorityCalculator {

    public static double calculateScore(String urgency, int peopleAffected, Timestamp requestedDate, String disasterSeverity) {
        double urgencyWeight;
        if ("CRITICAL".equalsIgnoreCase(urgency)) {
            urgencyWeight = 10.0;
        } else if ("HIGH".equalsIgnoreCase(urgency)) {
            urgencyWeight = 7.0;
        } else if ("MEDIUM".equalsIgnoreCase(urgency)) {
            urgencyWeight = 4.0;
        } else {
            urgencyWeight = 1.0;
        }

        // Urgency component (up to 400 points)
        double urgencyScore = urgencyWeight * 40.0;

        // People affected component (scaled at 0.35 per person)
        double populationScore = Math.max(0, peopleAffected) * 0.35;

        // Waiting time component (2 points per hour waiting)
        double waitingHours = 0.0;
        if (requestedDate != null) {
            long diffMs = System.currentTimeMillis() - requestedDate.getTime();
            waitingHours = Math.max(0, diffMs / (1000.0 * 60 * 60));
        }
        double waitingScore = Math.min(100.0, waitingHours * 2.0);

        // Disaster severity bonus
        double severityBonus = 0.0;
        if ("CRITICAL".equalsIgnoreCase(disasterSeverity)) {
            severityBonus = 25.0;
        } else if ("HIGH".equalsIgnoreCase(disasterSeverity)) {
            severityBonus = 15.0;
        } else if ("MEDIUM".equalsIgnoreCase(disasterSeverity)) {
            severityBonus = 5.0;
        }

        double totalScore = urgencyScore + populationScore + waitingScore + severityBonus;
        return Math.round(totalScore * 10.0) / 10.0;
    }

    public static double calculateScore(ResourceRequest request, String disasterSeverity) {
        if (request == null) return 0.0;
        return calculateScore(request.getUrgency(), request.getPeopleAffected(), request.getRequestedDate(), disasterSeverity);
    }
}
