package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import controlers.Controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Définit une fenêtre pour paramétrer & générer un bilan
 */
public class BilanGenerate extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField from;
	private JTextField to;

	public BilanGenerate(Controler controler) {
		setTitle("G\u00E9n\u00E9rer un bilan");
		setBounds(100, 100, 450, 231);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTextPane txtpnEnCliquantSur = new JTextPane();
			txtpnEnCliquantSur.setOpaque(false);
			txtpnEnCliquantSur.setEditable(false);
			txtpnEnCliquantSur.setText("En cliquant sur \"OK\", un bilan sera g\u00E9n\u00E9r\u00E9 au format HTML. Veuillez \u00E9crire entrer les dates pour la g\u00E9n\u00E9ration du bilan :");
			contentPanel.add(txtpnEnCliquantSur, BorderLayout.NORTH);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JLabel lblDu = new JLabel("Du");
				panel.add(lblDu);
			}
			{
				from = new JTextField();
				from.setText("01/01/2000");
				panel.add(from);
				from.setColumns(10);
			}
			{
				JLabel lblAu = new JLabel(" au ");
				panel.add(lblAu);
			}
			{
				to = new JTextField();
				to.setText("20/12/2050");
				panel.add(to);
				to.setColumns(10);
			}
		}
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							//On essaie
							Date d_from = format.parse(from.getText());
							Date d_to = format.parse(to.getText());
							controler.generateHTML(d_from, d_to);
							JOptionPane.showMessageDialog(null, "Un bilan général a été généré.\n\nPour le consulter, ouvrer bilan.html", "Edition d'un bilan", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						} catch (ParseException e1) {
							//sinon on ne fait rien
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
