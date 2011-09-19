package com.gfactor.osgi.api.export.obj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;

/**
 * @author momo
 *
 */
public class UserInfoObject extends User{
	private String mail;
	
	public UserInfoObject(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,accountNonLocked, authorities);
		
	}
	
	public static void main(String[] args) {
		List<GrantedAuthority> grant = new ArrayList<GrantedAuthority>();
		grant.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		grant.add(new GrantedAuthorityImpl("ROLE_USER"));
		
		UserInfoObject obj = new UserInfoObject("Momo","momo",false,false,false,false,grant);
		
		System.out.println("obj = "+ obj.toString());
		
		
	}
		
	
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		
		//Spring user object default value as below:
		sb.append("Username: ").append(this.getUsername()).append("; ");
		sb.append("Password: [PROTECTED]; ");
		sb.append("Enabled: ").append(this.isEnabled()).append("; ");
		sb.append("AccountNonExpired: ").append(this.isAccountNonExpired())
				.append("; ");
		sb.append("credentialsNonExpired: ").append(this.isCredentialsNonExpired())
				.append("; ");
		sb.append("AccountNonLocked: ").append(this.isAccountNonLocked())
				.append("; ");

		if (!this.getAuthorities().isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : this.getAuthorities()) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append("<").append(auth).append(">");
			}
		} else {
			sb.append("Not granted any authorities");
		}
		
		sb.append("; ");
		
		//user defined object value as below:
		sb.append("Mail: ").append(this.getMail()).append("; ");
		
		return sb.toString();
	}

}
