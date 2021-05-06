// This program converts an image from a URL or client's files and converts it into
// pixel art based on the client's selected pixel size

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // GUI Component
        JFrame frame = new JFrame("Pixel Art Converter");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Image URL:");
        JTextField text_field = new JTextField(10);
        JLabel label_2 = new JLabel("Or Select A File:");
        JButton open = new JButton("Open");
        JLabel label_3 = new JLabel("No File Selected");

        JPanel panel_2 = new JPanel();
        JLabel label_4 = new JLabel("Pixel Size:");
        JTextField text_field_2 = new JTextField(5);
        JButton save = new JButton("Save As:");

        JPanel panel_3 = new JPanel();
        JButton convert = new JButton("Pixelate");
        JLabel label_5 = new JLabel("Warning: No Save Destination Selected");

        panel.add(label);
        panel.add(text_field);
        panel.add(label_2);
        panel.add(open);
        panel.add(label_3);
        panel_2.add(label_4);
        panel_2.add(text_field_2);
        panel_2.add(save);
        panel_3.add(convert);
        panel_3.add(label_5);

        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, panel_2);
        frame.getContentPane().add(BorderLayout.SOUTH, panel_3);
        frame.setVisible(true);

        // Choose Image File
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                if (j.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    label_3.setText(j.getSelectedFile().getAbsolutePath());
                } else {
                    label_3.setText("No File Selected");
                }
            }
        });

        // Choose Output Destination
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                if (j.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    label_5.setText(j.getSelectedFile().getAbsolutePath());
                } else {
                    label_5.setText("Warning: No Save Destination Selected");
                }
            }
        });

        // Convert Image to Pixel Art
        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = null;
                if (!label_3.getText().equals("No File Selected")) {
                    File file = new File(label_3.getText());
                    try {
                        image = ImageIO.read(file);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    URL url = null;
                    try {
                        url = new URL(text_field.getText());
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }
                    try {
                        image = ImageIO.read(url);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                int width = image.getWidth();
                int height = image.getHeight();

                // write new image
                int size = Integer.parseInt(text_field_2.getText());
                for (int i = 0; i < width / size; i++) {
                    for (int j = 0; j < height / size; j++) {
                        int new_pixel = image.getRGB(i * size, j * size);
                        for (int k = 0; k < size; k++) {
                            for (int l = 0; l < size; l++) {
                                image.setRGB(i * size + l, j * size + k, new_pixel);
                            }
                        }
                    }
                }

                int new_pixel = 0;
                for (int i = 0; i < height - height % size; i++) {
                    for (int j = width - width % size; j < width; j++) {
                        if (j == width - width % size && i % size == 0) {
                            new_pixel = image.getRGB(j, i);
                        }
                        image.setRGB(j, i, new_pixel);
                    }
                }

                for (int i = 0; i < width; i++) {
                    for (int j = height - height % size; j < height; j++) {
                        if (j == height - height % size && i % size == 0) {
                            new_pixel = image.getRGB(i, j);
                        }
                        image.setRGB(i, j, new_pixel);
                    }
                }

                String output = label_5.getText();
                File output_file = new File(output);
                try {
                    ImageIO.write(image, "png", output_file);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
