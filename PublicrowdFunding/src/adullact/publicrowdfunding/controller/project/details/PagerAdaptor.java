package adullact.publicrowdfunding.controller.project.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Ferrand and Nelaupe
 */
public class PagerAdaptor extends FragmentStatePagerAdapter {

	private String idProject;

	private static PagerAdaptor _this;

	private String[] titles = { "Commentaires", "Détail du projet",
			"Emplacement du projet" };

	public PagerAdaptor(FragmentManager fm, String idProject) {
		super(fm);
		this.idProject = idProject;
		_this = this;
	}

	final int PAGE_COUNT = 3;

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		Bundle bundle = new Bundle();

		switch (position) {

		case 0:
			fragment = new ListCommentsFragment();
			bundle.putString("idProject", idProject);
			fragment.setArguments(bundle);
			fragment.setRetainInstance(false);
			return fragment;
		case 1:
			fragment = new InfoProjectFragment();
			bundle.putString("idProject", idProject);
			fragment.setArguments(bundle);
			fragment.setRetainInstance(false);
			return fragment;
		case 2:
			fragment = new MapFragment();
			bundle.putString("idProject", idProject);
			fragment.setArguments(bundle);
			fragment.setRetainInstance(false);
			return fragment;

		default:
			return new InfoProjectFragment();
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position % PAGE_COUNT];
	}

}
