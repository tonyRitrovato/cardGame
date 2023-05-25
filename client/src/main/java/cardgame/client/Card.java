package cardgame.client;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Card extends JToggleButton implements ActionListener, MouseListener{
    
    private String value;
    private String suite;
    private final int altezza = 200;
    private final int larghezza = 120;
    private Singleton s = Singleton.getInstance();
    private ImageIcon icon;

    public Card(String value, String suite) throws Exception {
        super();
        setValue(value);
        setSuite(suite);
        String imagePath = "./assets/" + value + suite + ".png"; // percorso dell'immagine all'interno del classpath
        Border border = BorderFactory.createLineBorder(new Color(40,40,40), 3);
        URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl == null) {
            // gestione dell'errore se l'immagine non viene trovata
            throw new Exception("Impossibile trovare l'immagine: " + imagePath);
        }
        addMouseListener(this);
        ImageIcon image = new ImageIcon(imgUrl);
        Image img = image.getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        setIcon(icon);
        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setBorder(border);
        setDisabledIcon(icon);
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        try {
            /*invia la carta */
            if(s.getTurn() == true) {
                System.out.println(s.getCS().risposta("GC" + s.getId() + ',' + suite + value));
                s.setTurn(false);
                s.getTableGame().deck.remove(this);
                s.getTableGame().deck.repaint();
                s.getTableGame().cardsOnTable.add(this);
                s.getTableGame().cardsOnTable.revalidate();
                String tableCard = s.getCS().risposta("ST" + s.getId());
                tableCard = tableCard.substring(2, tableCard.length());
                s.getTableGame().cardsOnTable.removeAll();
                if(tableCard.length() > 0) {
                    for(int i = 0; i < tableCard.length(); i += 2) {
                        Card c = new Card(tableCard.substring(i+1, i+2), tableCard.substring(i, i+1));
                        c.setEnabled(false);
                        c.removeMouseListener();
                        s.getTableGame().cardsOnTable.add(c);
                        s.getTableGame().cardsOnTable.revalidate();
                        s.getTableGame().cardsOnTable.repaint();
                    }
                }
                s.geTimer().start();
            }
            else
                JOptionPane.showMessageDialog(null, "non è il tuo turno", "avviso", JOptionPane.WARNING_MESSAGE);
        }
        catch(Exception er) {
            er.printStackTrace();
        }
    }
    

    public void setValue(String value) {
        this.value = value;
    } 

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getValue() {
        return value;
    }

    public String getSuite() {
        return suite;
    }

    public void removeMouseListener() {
        removeMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }    

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 3)); 
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(BorderFactory.createLineBorder(new Color(40,40,40), 3));
    }
}

