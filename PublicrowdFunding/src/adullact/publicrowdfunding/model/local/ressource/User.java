package adullact.publicrowdfunding.model.local.ressource;

import java.util.ArrayList;
import java.util.Map;

import adullact.publicrowdfunding.model.local.cache.Cache;
import adullact.publicrowdfunding.model.local.cache.CacheSet;
import adullact.publicrowdfunding.model.local.callback.HoldToDo;
import adullact.publicrowdfunding.model.local.callback.WhatToDo;
import adullact.publicrowdfunding.model.server.entities.DetailedServerUser;
import adullact.publicrowdfunding.model.server.entities.RowAffected;
import adullact.publicrowdfunding.model.server.entities.ServerBookmark;
import adullact.publicrowdfunding.model.server.entities.ServerUser;
import adullact.publicrowdfunding.model.server.entities.Service;
import adullact.publicrowdfunding.model.server.entities.SimpleServerResponse;
import rx.Observable;

/**
 * 
 * @author Ferrand
 * 
 */
public class User extends Resource<User, ServerUser, DetailedServerUser> {
	private String m_pseudo;
	private String m_name;
	private String m_firstName;
	private String m_city;
	private String m_gender;
	private CacheSet<Bookmark> m_bookmark;
	private CacheSet<Funding> m_funding;

    /* ----- Resource ----- */
    @Override
    public String getResourceId() {
        return m_pseudo;
    }

    @Override
    public void setResourceId(String id) {
        this.m_pseudo = id;
    }

    @Override
    public ServerUser toServerResource() {
        ServerUser serverUser = new ServerUser();
        serverUser.pseudo = getResourceId();
        serverUser.mail = "";
        serverUser.name = this.m_name;
        serverUser.firstName = this.m_firstName;
        serverUser.city = this.m_city;
        serverUser.sexe = this.m_gender;
    
        return serverUser;
    }

    @Override
    public User makeCopyFromServer(ServerUser serverUser) {
        User user = new User();
        if((!(user.m_pseudo.equals(serverUser.pseudo)) || !(user.m_name.equals(serverUser.name)) || !user.m_firstName.equals(serverUser.firstName))) {
            changed();
        }

        user.m_pseudo = serverUser.pseudo;
        user.m_name = serverUser.name;
        user.m_firstName = serverUser.firstName;
        user.m_city = serverUser.city;
        user.m_gender = serverUser.sexe;
        user.m_bookmark = new CacheSet<Bookmark>();

        return user;
    }

    @Override
    public User syncFromServer(DetailedServerUser detailedServerUser) {
        if((!(m_pseudo.equals(detailedServerUser.pseudo)) || !(m_name.equals(detailedServerUser.name)) || !m_firstName.equals(detailedServerUser.firstName))) {
            changed();
        }

        this.m_pseudo = detailedServerUser.pseudo;
        this.m_name = detailedServerUser.name;
        this.m_firstName = detailedServerUser.firstName;
        this.m_city = detailedServerUser.city;
        this.m_gender = detailedServerUser.sexe;
        this.m_bookmark = new CacheSet<Bookmark>();

        for(final ServerBookmark serverBookmark : detailedServerUser.bookmarkedProjects) {
            final Cache<Bookmark> bookmarks = new Bookmark().getCache(Integer.toString(serverBookmark.id)).declareUpToDate();
            bookmarks.toResource(new HoldToDo<Bookmark>() {
                @Override
                public void hold(Bookmark resource) {
                    resource.syncFromServer(serverBookmark);
                    m_bookmark.add(bookmarks);
                }
            });
        }
        return this;
    }

    /* ------ Method ------ */
    @Override
    public Observable<DetailedServerUser> methodGET(Service service) {
        return service.detailUser(m_pseudo);
    }

    @Override
    public Observable<ArrayList<ServerUser>> methodGETAll(Service service, Map<String, String> filter) {
        return service.listUsers(filter);
    }

    @Override
    public Observable<SimpleServerResponse> methodPUT(Service service) {
        return service.modifyUser(toServerResource(), m_pseudo);
    }

    @Override
    public Observable<RowAffected> methodPOST(Service service){
        return service.createUser(toServerResource());
    }

    @Override
    public Observable<SimpleServerResponse> methodDELETE(Service service) {
        return service.deleteUser(getResourceId());
    }
    /* -------------------- */
	
	public User() {
		this.m_pseudo = null;
		this.m_name = null;
		this.m_firstName = null;
		this.m_city = null;
		this.m_gender = null;

	}

    public User(String pseudo, String name, String firstName, String city, String sexe) {
        this.m_pseudo = pseudo;
        this.m_name = name;
        this.m_firstName = firstName;
        this.m_city = city;
        this.m_gender = sexe;

    }

	/* Getter */
	public String getName() {
		return m_name;
	}

	public String getFirstName() {
		return m_firstName;
	}

	public String getPseudo() {
		return m_pseudo;
	}
	
	public String getCity() {
		return m_city;
	}
	
	public String getGender(){
		return m_gender;
	}
	
	public void getBookmarkedProjects(final WhatToDo<Bookmark> projectWhatToDo) {
	      m_bookmark.forEach(projectWhatToDo);
	}
	
	public void getFundingProjects(final WhatToDo<Funding> projectWhatToDo) {
        m_funding.forEach(projectWhatToDo);
    }


	/* Setters */
    public void setPseudo(String pseudo) {
        m_pseudo = pseudo;
    }

    public void setName(String name){
        m_name = name;
    }

    public void setFirstName(String firstName) {
        m_firstName = firstName;
    }
    
    public void setCity(String city){
    	m_city = city;
    }
    
    public void setGender(String gender){
    	m_gender = gender;
    }
    /* ------- */
    
    public String toString(){
    	return "Pseudo:"+m_pseudo+
    			"Name:"+m_name+
    			"First Name:"+m_firstName;
    }
}
