package com.helixbeat.provider.dataservice;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
 
@Component
public class JerseyConfig extends ResourceConfig 
{
    public JerseyConfig() 
    {
        register(UserResource.class);
        register(OrganizationResource.class);
        register(WorkspaceResource.class);
        register(RunResource.class);
        register(DashboardGraphResource.class);
        register(new CorsFilter());
    }
}