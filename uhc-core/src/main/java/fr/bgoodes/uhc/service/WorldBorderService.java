package fr.bgoodes.uhc.service;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class WorldBorderService {

    private double radius = Double.POSITIVE_INFINITY;
    private double targetRadius = Double.POSITIVE_INFINITY;
    private double speed;

    public synchronized double getRadius() {
        return radius;
    }

    public synchronized void setRadius(double radius) {
        this.radius = radius;
        this.targetRadius = radius;
        this.speed = 0;
    }

    public synchronized void shrinkTo(double targetRadius, double speed) {
        this.targetRadius = targetRadius;
        this.speed = speed;
    }

    public synchronized boolean update(double seconds) {
        if (radius <= targetRadius) {
            return false;
        }

        radius = Math.max(targetRadius, radius - speed * seconds);
        return radius == targetRadius;
    }

    public synchronized boolean isOutside(@NotNull Vector3dc position) {
        return Math.abs(position.x()) > radius || Math.abs(position.z()) > radius;
    }
}
