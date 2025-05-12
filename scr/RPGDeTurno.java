import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RPGDeTurno {
    private JFrame frame;
    private JLabel heroiLabel, monstroLabel, statusLabel, iniciativaLabel, dadoLabel;
    private JTextField vidaHeroiField, bonusAtaqueHeroiField, vidaMonstroField, bonusAtaqueMonstroField;
    private Personagem heroi, monstro;
    private ListaTurno turnos;
    private Random random = new Random();
    private JButton atacarButton;

    public RPGDeTurno() {
        frame = new JFrame("RPG por Turnos - Estilo Mesa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLayout(new BorderLayout());

        JPanel configPanel = criarPainelConfiguracao();
        frame.add(configPanel, BorderLayout.NORTH);

        JPanel personagensPanel = criarPainelPersonagens();
        frame.add(personagensPanel, BorderLayout.CENTER);

        JPanel bottomPanel = criarPainelInferior();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel criarPainelConfiguracao() {
        JPanel panel = new JPanel(new GridLayout(2, 3));
        
        vidaHeroiField = new JTextField("0", 5);
        bonusAtaqueHeroiField = new JTextField("0", 5);
        vidaMonstroField = new JTextField("0", 5);
        bonusAtaqueMonstroField = new JTextField("0", 5);
        
        panel.add(new JLabel("Vida Herói:"));
        panel.add(vidaHeroiField);
        panel.add(new JLabel("Bônus Ataque Herói:"));
        panel.add(bonusAtaqueHeroiField);
        panel.add(new JLabel("Vida Monstro:"));
        panel.add(vidaMonstroField);
        panel.add(new JLabel("Bônus Ataque Monstro:"));
        panel.add(bonusAtaqueMonstroField);

        JButton iniciarButton = new JButton("Iniciar Batalha");
        iniciarButton.addActionListener(e -> iniciarBatalha());
        panel.add(iniciarButton);

        return panel;
    }

    private JPanel criarPainelPersonagens() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        ImageIcon heroiIcon = loadAndResizeImage("/assets/Yugs.png", 250, 250);
        ImageIcon monstroIcon = loadAndResizeImage("/assets/Gnoll.png", 200, 200);

        heroiLabel = new JLabel(heroiIcon);
        monstroLabel = new JLabel(monstroIcon);
        panel.add(heroiLabel);
        panel.add(monstroLabel);

        return panel;
    }

    private JPanel criarPainelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        statusLabel = new JLabel("Configure os valores e clique em Iniciar");
        iniciativaLabel = new JLabel("", SwingConstants.CENTER);
        dadoLabel = new JLabel("", SwingConstants.CENTER);
        
        infoPanel.add(statusLabel);
        infoPanel.add(iniciativaLabel);
        infoPanel.add(dadoLabel);

        atacarButton = new JButton("Atacar");
        atacarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        atacarButton.setMaximumSize(new Dimension(200, 40));
        atacarButton.setFont(new Font("Arial", Font.BOLD, 14));
        atacarButton.setBackground(new Color(70, 130, 180));
        atacarButton.setForeground(Color.WHITE);
        atacarButton.setVisible(false);
        atacarButton.addActionListener(e -> executarTurno());

        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(atacarButton);

        return panel;
    }

    private ImageIcon loadAndResizeImage(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar imagem: " + path);
            return new ImageIcon();
        }
    }

    private void iniciarBatalha() {
        try {
            int vidaHeroi = Integer.parseInt(vidaHeroiField.getText());
            int bonusHeroi = Integer.parseInt(bonusAtaqueHeroiField.getText());
            int vidaMonstro = Integer.parseInt(vidaMonstroField.getText());
            int bonusMonstro = Integer.parseInt(bonusAtaqueMonstroField.getText());
    
            heroi = new Personagem("Herói", vidaHeroi, bonusHeroi);
            monstro = new Personagem("Monstro", vidaMonstro, bonusMonstro);
    
            int iniciativaHeroi = rolarDado(20) + heroi.getAtaque();
            int iniciativaMonstro = rolarDado(20) + monstro.getAtaque();
            
            String resultadoIniciativa;
            if (iniciativaHeroi > iniciativaMonstro) {
                resultadoIniciativa = "Herói começa! (Iniciativa: " + iniciativaHeroi + " vs " + iniciativaMonstro + ")";
                turnos = new ListaTurno();
                turnos.adicionar(heroi);
                turnos.adicionar(monstro);
            } else if (iniciativaMonstro > iniciativaHeroi) {
                resultadoIniciativa = "Monstro começa! (Iniciativa: " + iniciativaMonstro + " vs " + iniciativaHeroi + ")";
                turnos = new ListaTurno();
                turnos.adicionar(monstro);
                turnos.adicionar(heroi);
            } else {
                resultadoIniciativa = "Empate! (Iniciativa: " + iniciativaHeroi + ") - Herói começa";
                turnos = new ListaTurno();
                turnos.adicionar(heroi);
                turnos.adicionar(monstro);
            }
            
            iniciativaLabel.setText(resultadoIniciativa);
            statusLabel.setText("Batalha iniciada! Pressione Atacar.");
            dadoLabel.setText("");
    
            frame.getContentPane().remove(0); 
            atacarButton.setVisible(true);
            frame.revalidate();
            frame.repaint();
    
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Digite valores numéricos válidos!");
        }
    }

    private int rolarDado(int lados) {
        return random.nextInt(lados) + 1;
    }

    private void executarTurno() {
        if (turnos == null) return;

        Personagem atacante = turnos.proximo();
        Personagem alvo = (atacante == heroi) ? monstro : heroi;
        
        int resultadoDado = rolarDado(20);
        int totalAtaque = resultadoDado + atacante.getAtaque();
        int dano = 0;
        
        String mensagemDado = "Dado: " + resultadoDado + " + Bônus: " + atacante.getAtaque() + " = " + totalAtaque;
        
        if (totalAtaque >= 11) {
            dano = rolarDado(8);
            alvo.receberDano(dano);
            mensagemDado += " | Acerto! Dano: " + dano;
        } else {
            mensagemDado += " | Errou!";
        }
        
        dadoLabel.setText(mensagemDado);
        statusLabel.setText(atacante.getNome() + " atacou! " + alvo.getStatus());
        
        if (!alvo.estaVivo()) {
            statusLabel.setText(alvo.getNome() + " foi derrotado!");
            atacarButton.setEnabled(false);
        }
        
        turnos.adicionar(atacante);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RPGDeTurno());
    }
}