package net.andrewcpu.gui;

import net.andrewcpu.gui.state.BehaviorState;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;

public class IconItem {
        private String name;
        private ImageIcon icon;
        private String filePath;

        public IconItem(String filePath) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
                BehaviorState state = (BehaviorState) in.readObject();
                this.name = state.getName();
            } catch (Exception e) {

            }
            this.filePath = filePath;
            String iconPath = "/cube.png";
            URL imgUrl = getClass().getResource(iconPath);
            if (imgUrl != null) {
                ImageIcon tempIcon = new ImageIcon(imgUrl);
                Image img = tempIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                this.icon = new ImageIcon(img);
            } else {
                System.err.println("Couldn't find file: " + iconPath);
                this.icon = new ImageIcon(); // Maybe load a default image or leave it blank
            }
        }

        public String getName() {
            return name;
        }

        public ImageIcon getIcon() {
            return icon;
        }

        public String getFilePath() {
            return filePath;
        }
    }

