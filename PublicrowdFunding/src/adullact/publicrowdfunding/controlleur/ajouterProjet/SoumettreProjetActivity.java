package adullact.publicrowdfunding.controlleur.ajouterProjet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adullact.publicrowdfunding.R;
import adullact.publicrowdfunding.custom.CarouselAdapter;
import adullact.publicrowdfunding.exception.NoAccountExistsInLocal;
import adullact.publicrowdfunding.model.local.callback.WhatToDo;
import adullact.publicrowdfunding.model.local.ressource.Account;
import adullact.publicrowdfunding.model.local.ressource.Project;
import adullact.publicrowdfunding.model.local.ressource.User;
import adullact.publicrowdfunding.model.local.utilities.Utility;
import adullact.publicrowdfunding.model.server.event.CreateEvent;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.touchmenotapps.carousel.simple.HorizontalCarouselLayout;
import com.touchmenotapps.carousel.simple.HorizontalCarouselLayout.CarouselInterface;
import com.touchmenotapps.carousel.simple.HorizontalCarouselStyle;

public class SoumettreProjetActivity extends Activity {

	private EditText m_titre;
	private EditText m_Description;
	private DatePicker m_dateFin;
	private Button m_localisation;
	private EditText m_edit_text_somme;
	private Context context;
	private TextView m_user_pseudo;
	private int m_illustration;

	private LatLng position;

	private User user;

	private static final int PICK_MAPS = 3;

	private HorizontalCarouselStyle mStyle;
	private HorizontalCarouselLayout mCarousel;
	private CarouselAdapter mAdapter;
	private ArrayList<Integer> mData = new ArrayList<Integer>(0);

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void supprimerCalendarView() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			m_dateFin.setCalendarViewShown(false);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_projet);

		
		try {
			Account compte = Account.getOwn();
			compte.getUser(new WhatToDo<User>() {

				@Override
				public void hold(User resource) {
					if(resource == null){
						finish();
					}
					user = resource;
					System.out.println(user.getResourceId());
				}

				@Override
				public void eventually() {
					// TODO Auto-generated method stub

				}

			});
		} catch (NoAccountExistsInLocal e) {
			finish();
		}
		
		m_illustration = 0;

		context = this;

		m_titre = (EditText) findViewById(R.id.titre);
		m_Description = (EditText) findViewById(R.id.description);
		m_dateFin = (DatePicker) findViewById(R.id.date_de_fin);
		m_localisation = (Button) findViewById(R.id.button_localisation);
		m_edit_text_somme = (EditText) findViewById(R.id.edit_text_somme);
		m_user_pseudo = (TextView) findViewById(R.id.utilisateur_soumission);

		try {
			Account count = Account.getOwn();
			m_user_pseudo.setText(count.getResourceId());
		} catch (NoAccountExistsInLocal e) {
			finish();
		}

		mData.add(R.drawable.ic_launcher);
		mData.add(R.drawable.basketball);
		mData.add(R.drawable.roi);

		mAdapter = new CarouselAdapter(this);
		mAdapter.setData(mData);
		mCarousel = (HorizontalCarouselLayout) findViewById(R.id.carousel_layout);
		mStyle = new HorizontalCarouselStyle(this,
				HorizontalCarouselStyle.NO_STYLE);
		mCarousel.setStyle(mStyle);
		mCarousel.setAdapter(mAdapter);

		mCarousel.setOnCarouselViewChangedListener(new CarouselInterface() {

			@Override
			public void onItemChangedListener(View v, int position) {
				m_illustration = position;
			}
		});

		m_localisation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getBaseContext(),
						AddLocationProject.class);
				if (position != null) {
					in.putExtra("latitude", position.latitude);
					in.putExtra("longitude", position.longitude);
				}

				startActivityForResult(in, 3);
			}
		});

		supprimerCalendarView();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {

		case PICK_MAPS:
			Bundle extras3 = data.getExtras();
			if (extras3 != null) {

				position = new LatLng(extras3.getDouble("latitude"),
						extras3.getDouble("longitude"));
				Toast.makeText(getApplicationContext(),
						"Position du projet ajouté", Toast.LENGTH_SHORT).show();
				m_localisation.setText("Votre projet est localisé");
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.soumission, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.soumettre:

			String titre = null;
			if (m_titre.length() == 0) {
				Toast.makeText(context, "Merci de mettre un titre",
						Toast.LENGTH_SHORT).show();
				return false;
			}
			titre = m_titre.getText().toString();

			String description = null;
			if (m_Description.length() == 0) {
				Toast.makeText(context, "Merci de mettre une description",
						Toast.LENGTH_SHORT).show();
				return false;
			}
			description = m_Description.getText().toString();

			Date now = new Date();

			Date date_fin = new Date();
			int day = m_dateFin.getDayOfMonth();
			int month = m_dateFin.getMonth();
			int year = m_dateFin.getYear();
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			date_fin = calendar.getTime();

			new Project(titre, 
					description, 
					user.getResourceId(), 
					"10000",
					Utility.stringToDateTime("2014-09-04 00:00:00"),
					Utility.stringToDateTime("2014-10-04 00:00:00"),
					new LatLng(position.latitude, position.longitude),
					m_illustration, "","","").serverCreate(new CreateEvent<Project>() {
				@Override
				public void errorResourceIdAlreadyUsed() {
					System.out.println("id déja utilisé");
				}

				@Override
				public void onCreate(Project resource) {
					System.out.println("Creation du compte");
				}

				@Override
				public void errorAuthenticationRequired() {
					System.out.println("Auth required");
				}

				@Override
				public void errorNetwork() {
					System.out.println("network error");
				}
			});

			return true;

		}
		return false;

	}
}
