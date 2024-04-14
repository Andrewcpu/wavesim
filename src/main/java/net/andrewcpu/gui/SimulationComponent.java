package net.andrewcpu.gui;

import net.andrewcpu.Pond;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimulationComponent extends JComponent implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {
    public Pond pond;
    private boolean mouseDown = false;
    private int mouseX = 0, mouseY = 0;
    private int prevMouseX = 0, prevMouseY = 0;
    private int prevToggledX = -1, prevToggledY = -1;
    private double zoomLevel = 1.0;
    private int panX = 0, panY = 0;
    private double minZoomLevel = 1.0;

    public SimulationComponent() {
        this.pond = new Pond();
        addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Calculate the scaled dimensions
        int imageWidth = Pond.POND_SIZE * Pond.TILE_SIZE;
        int imageHeight = Pond.POND_SIZE * Pond.TILE_SIZE;
        double scaledWidth = imageWidth * zoomLevel;
        double scaledHeight = imageHeight * zoomLevel;

        // Calculate the position to center the image
        double scaledX = (getWidth() - scaledWidth) / 2.0 + panX;
        double scaledY = (getHeight() - scaledHeight) / 2.0 + panY;

        // Draw the scaled and positioned image
        g2d.drawImage(this.pond.render(), (int) scaledX, (int) scaledY, (int) scaledWidth, (int) scaledHeight, null);

        // Draw the cursor
        int cursorX = (int) ((pond.getCursorX() * Pond.TILE_SIZE * zoomLevel) + scaledX);
        int cursorY = (int) ((pond.getCursorY() * Pond.TILE_SIZE * zoomLevel) + scaledY);
        g2d.setColor(Color.white);
        g2d.fillRect(cursorX, cursorY, Pond.TILE_SIZE, Pond.TILE_SIZE);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoomFactor = 1.1;
        double prevZoomLevel = zoomLevel;
        requestFocusInWindow();
        if (e.getWheelRotation() < 0) {
            zoomLevel *= zoomFactor;
        } else {
            zoomLevel /= zoomFactor;
        }

        // Calculate the minimum zoom level based on the component size and image dimensions
        int imageWidth = Pond.POND_SIZE * Pond.TILE_SIZE;
        int imageHeight = Pond.POND_SIZE * Pond.TILE_SIZE;
        double minZoomX = (double) getWidth() / imageWidth;
        double minZoomY = (double) getHeight() / imageHeight;
        minZoomLevel = Math.max(minZoomX, minZoomY);

        // Limit the zoom level to be at least the minimum zoom level
        zoomLevel = Math.max(minZoomLevel, zoomLevel);

        // Calculate the mouse position relative to the component
        double mouseX = e.getX();
        double mouseY = e.getY();

        // Calculate the mouse position relative to the scaled and panned image
        double scaledWidth = imageWidth * prevZoomLevel;
        double scaledHeight = imageHeight * prevZoomLevel;
        double prevScaledX = (getWidth() - scaledWidth) / 2.0 + panX;
        double prevScaledY = (getHeight() - scaledHeight) / 2.0 + panY;
        double imageX = (mouseX - prevScaledX) / prevZoomLevel;
        double imageY = (mouseY - prevScaledY) / prevZoomLevel;

        // Calculate the new scaled dimensions
        scaledWidth = imageWidth * zoomLevel;
        scaledHeight = imageHeight * zoomLevel;

        // Calculate the new panning values to keep the mouse position at the same image coordinates
        panX = (int) (mouseX - imageX * zoomLevel - (getWidth() - scaledWidth) / 2.0);
        panY = (int) (mouseY - imageY * zoomLevel - (getHeight() - scaledHeight) / 2.0);

        // Limit the pan to keep the image within the frame
        int maxPanX = (int) Math.max(0, (scaledWidth - getWidth()) / 2.0);
        int maxPanY = (int) Math.max(0, (scaledHeight - getHeight()) / 2.0);
        panX = Math.max(-maxPanX, Math.min(maxPanX, panX));
        panY = Math.max(-maxPanY, Math.min(maxPanY, panY));

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }
        this.pond.keyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        if (shiftPressed) {
            prevMouseX = e.getX();
            prevMouseY = e.getY();
        }
        requestFocusInWindow();
    }

    private boolean shiftPressed = false;

    @Override
    public void mouseDragged(MouseEvent e) {
        requestFocusInWindow();

        if (mouseDown && !shiftPressed) {
            // Calculate the scaled dimensions and positions
            int imageWidth = Pond.POND_SIZE * Pond.TILE_SIZE;
            int imageHeight = Pond.POND_SIZE * Pond.TILE_SIZE;
            double scaledWidth = imageWidth * zoomLevel;
            double scaledHeight = imageHeight * zoomLevel;
            double scaledX = (getWidth() - scaledWidth) / 2.0 + panX;
            double scaledY = (getHeight() - scaledHeight) / 2.0 + panY;

            // Calculate the grid coordinates of the mouse position
            int mouseX = (int) ((e.getX() - scaledX) / (zoomLevel * Pond.TILE_SIZE));
            int mouseY = (int) ((e.getY() - scaledY) / (zoomLevel * Pond.TILE_SIZE));

            if (mouseX != prevToggledX || mouseY != prevToggledY) {
                try {
                    pond.currentMaterial = pond.behaviorPanel.build();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                pond.toggleSolidObject(mouseX, mouseY);
                prevToggledX = mouseX;
                prevToggledY = mouseY;
            }
        } else if (shiftPressed) {
            int dx = e.getX() - prevMouseX;
            int dy = e.getY() - prevMouseY;

            // Adjust the pan based on the mouse movement
            panX += dx;
            panY += dy;

            // Limit the pan to keep the image within the frame
            int imageWidth = Pond.POND_SIZE * Pond.TILE_SIZE;
            int imageHeight = Pond.POND_SIZE * Pond.TILE_SIZE;
            double scaledWidth = imageWidth * zoomLevel;
            double scaledHeight = imageHeight * zoomLevel;
            int maxPanX = (int) Math.max(0, (scaledWidth - getWidth()) / 2.0);
            int maxPanY = (int) Math.max(0, (scaledHeight - getHeight()) / 2.0);
            panX = Math.max(-maxPanX, Math.min(maxPanX, panX));
            panY = Math.max(-maxPanY, Math.min(maxPanY, panY));

            prevMouseX = e.getX();
            prevMouseY = e.getY();
            repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        prevToggledX = -1;
        prevToggledY = -1;
        requestFocusInWindow();

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocusInWindow();

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        requestFocusInWindow();

        // Calculate the scaled dimensions and positions
        int imageWidth = Pond.POND_SIZE * Pond.TILE_SIZE;
        int imageHeight = Pond.POND_SIZE * Pond.TILE_SIZE;
        int scaledWidth = (int) (imageWidth * zoomLevel);
        int scaledHeight = (int) (imageHeight * zoomLevel);
        int scaledX = (getWidth() - scaledWidth) / 2 + panX;
        int scaledY = (getHeight() - scaledHeight) / 2 + panY;

        // Calculate the cursor position relative to the scaled and panned image
        int cursorX = (int) ((e.getX() - scaledX) / zoomLevel / Pond.TILE_SIZE);
        int cursorY = (int) ((e.getY() - scaledY) / zoomLevel / Pond.TILE_SIZE);

        pond.setCursorX(cursorX);
        pond.setCursorY(cursorY);
    }

}
