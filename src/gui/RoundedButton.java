package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private static final int RADIUS = 30;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.BLACK);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(Color.ORANGE);
    }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            boolean hover = getModel().isRollover();
            g2.setColor(hover ? Color.WHITE : getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            g2.dispose();

            setForeground(hover ? Color.ORANGE : Color.BLACK);
            super.paintComponent(g);
	
    }
        

    @Override
    protected void paintBorder(Graphics g) {
        // Kenarlık çizilmesin
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
