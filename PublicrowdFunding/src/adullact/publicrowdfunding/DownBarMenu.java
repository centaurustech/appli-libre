package adullact.publicrowdfunding;

import adullact.publicrowdfunding.controlleur.ajouterProjet.SoumettreProjetActivity;
import adullact.publicrowdfunding.controlleur.membre.ConnexionActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class DownBarMenu extends Activity {
	
	private ImageButton m_ajouter_projet;
	private ImageButton m_mon_compte;
	private ImageButton m_rechercher;

	public void addDownBarMenu() {

		m_ajouter_projet = (ImageButton) findViewById(R.id.button_soumettre_projet);
		m_ajouter_projet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent in = new Intent(getBaseContext(),
						SoumettreProjetActivity.class);
				startActivity(in);

			}
		});

		m_mon_compte = (ImageButton) findViewById(R.id.button_mon_compte);
		m_mon_compte.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getBaseContext(),
						ConnexionActivity.class);
				startActivity(in);
			}
		});

		m_rechercher = (ImageButton) findViewById(R.id.button_search);
		m_rechercher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchDialog alertDialogBuilder = new SearchDialog(
						DownBarMenu.this);
				alertDialogBuilder.show();

			}
		});
	}

}
