/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services.service;

import com.model.Empleado;
import com.model.Empresa;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jhoselin Trujillo
 */
@Stateless
@Path("com.model.empleado")
public class EmpleadoFacadeREST extends AbstractFacade<Empleado> {

    @PersistenceContext(unitName = "EmpresaPU")
    private EntityManager em;
    @EJB
    EmpresaFacadeREST empresaFacadeREST;

    public EmpleadoFacadeREST() {
        super(Empleado.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Empleado entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Empleado entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Empleado find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Empleado> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Empleado> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("crear")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public String crear(@FormParam("nombre") String nombre, @FormParam("apellido") String apellido, @FormParam("cargo") String cargo, @FormParam("trabaja") boolean trabaja, @FormParam("idEmpresa") int empresa) {
        String mensaje = "{\"exitoso\":false}";
        Empresa emp=empresaFacadeREST.find(empresa);
        try {
            create(new Empleado(nombre, apellido, cargo, trabaja, emp));
            mensaje = "{\"exitoso\":true}";
        } catch (Exception e) {
            System.out.println(e);
        }

        return mensaje;
    }
      @POST
    @Path("editar")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
     public String editarE(@FormParam("nombre") String nombre, @FormParam("apellido") String apellido, @FormParam("cargo") String cargo, @FormParam("trabaja") boolean trabaja, @FormParam("idEmpresa") int idempresa) {
         String mensaje="{\"exitoso\":false,\"motivo\":";
         Empleado u= editarE(idempresa); 
         
         if (u != null){
             u.setNombre(nombre);
             u.setApellido(apellido);
             u.setCargo(cargo);
             u.setTrabaja(trabaja);
             u.setIdEmpresa(idempresa);
         try{
           edit(u);
           mensaje="{\"exitoso\":true";
         }catch(Exception e){
             mensaje+="\"Excepcion en base\"";
         }
         }else{
             mensaje+="\"datos no correctos\"";
         }
         mensaje+="}";
         return mensaje;
     }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
 public Empleado editarE(int idUsuario){
      
        TypedQuery<Empleado> qry;
        qry = getEntityManager().createQuery("SELECT e FROM Empleado e WHERE e.nombre = :nombre AND e.apellido = :apellido AND e.cargo = :cargo AND e.trabaja = :trabaja",Empleado.class);
        qry.setParameter("idusuario",idUsuario);
        qry.setParameter("eliminado", false);
        try{
            return qry.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
        
    }
}
