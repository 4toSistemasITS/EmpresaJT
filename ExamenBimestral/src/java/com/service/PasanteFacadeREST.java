/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Carrera;
import com.model.Pasante;
import com.model.Pasantia;
import java.util.Date;
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
@Path("com.model.pasante")
public class PasanteFacadeREST extends AbstractFacade<Pasante> {

    @PersistenceContext(unitName = "ExamenBimestralPU")
    private EntityManager em;
    //---------------se instancia
    @EJB
    PasantiaFacadeREST pasantiaFacadeREST;
    @EJB
    CarreraFacadeREST carreraFacadeREST;

    public PasanteFacadeREST() {
        super(Pasante.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Pasante entity) {
        super.create(entity);
    }
    //--------------------CREAR PASANTE-----------------------
    @POST
    @Path("crear")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public String crear(@FormParam("nombre") String nombre, @FormParam("apellido") String apellido,@FormParam("cedula") String cedula,
        @FormParam("edad") int edad,@FormParam("idpasantia") int id_pasantia,@FormParam("idcarrera") int id_carrera) {
        String mensaje="{\"exitoso\":false}";
        Pasantia p= pasantiaFacadeREST.find(id_pasantia);
        Carrera c= carreraFacadeREST.find(id_carrera);
        try{
            create(new Pasante(nombre,apellido,cedula,edad,false,new Date(),c,p));
            mensaje="{\"exitoso\":true}"; 
                  
        }catch(Exception e){           
        }
            
        return mensaje;
    }
     //---------------------------LEER PASANTE--------------------------------------------
    @POST
    @Path("consulta")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public List<Pasante> consulta(@FormParam("apellido")String apellido) {
         List<Pasante> retorno=leerPasante(apellido);
         return retorno;
    }
    //--------------------ACTUALIZAR PASANTE-------------------------------------------
    @POST
    @Path("editar")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public String editar(@FormParam("id") int id,@FormParam("nombre") String nombre, @FormParam("apellido") String apellido,@FormParam("cedula") String cedula,
            @FormParam("edad") int edad, @FormParam("psante") int id_pasantia,@FormParam("carrera") int id_carrera) {
        String mensaje="{\"exitoso\":false,\"motivo\":";
        Pasante pa= buscar(id);
        Pasantia pas= pasantiaFacadeREST.find(id_pasantia);
        Carrera ca= carreraFacadeREST.find(id_carrera);
        if(pa!=null){
            pa.setNombre(nombre);
            pa.setApellido(apellido);
            pa.setCedula(cedula);
            pa.setEdad(edad);
            pa.setEliminado(false);
            pa.setFechaRegistro(new Date());
            pa.setIdPasantia(pas);
            pa.setIdCarrera(ca);
            try {
                edit(pa);
                mensaje = "{\"exitoso\":true";
            } catch (Exception e) {
                mensaje += "\"Execpcion en base\"";
            }
        }else{
            mensaje+="\"Datos no correctos\"";
        }
        mensaje+="}";   
        return mensaje;
    }
    //--------------------ELIMINAR PASANTE---------------------------------------------//
    @POST
    @Path("eliminar")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public String eliminar(@FormParam("id") int id){
        String mensaje="{\"exitoso\":false,\"motivo\":";
        Pasante p= buscar(id);
        if(p!=null){
            p.setEliminado(true);
            try {
                edit(id, p);
                mensaje = "{\"exitoso\":true";
            } catch (Exception e) {
                mensaje += "\"Execpcion en base\"";
            }
        }else{
            mensaje+="\"Datos no correctos\"";
        }
        mensaje+="}";   
        return mensaje;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Pasante entity) {
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
    public Pasante find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Pasante> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Pasante> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    //Metodos
     public Pasante crear(String nombre){
        Pasante d=null;
        TypedQuery<Pasante> qry;
        qry = getEntityManager().createQuery("SELECT p FROM Pasante p WHERE p.nombre = :nombre AND p.eliminado = :eliminado", Pasante.class);
        qry.setParameter("nombre",nombre);
        qry.setParameter("eliminado",false);
         try {
            return qry.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
        List<Pasante> leerPasante(String valor) {
        TypedQuery<Pasante> qry;
        qry = getEntityManager().createQuery("SELECT p FROM Pasante p WHERE p.apellido = :apellido", Pasante.class);
        qry.setParameter("apellido", valor);
        try {
            return qry.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    } 
         public Pasante buscar(int pk_usuario){
        Pasante u=null;
        TypedQuery<Pasante> qry;
        qry = getEntityManager().createQuery("SELECT p FROM Pasante p WHERE p.idPasante = :idPasante", Pasante.class);
        qry.setParameter("idPasante",pk_usuario);
                
         try {
            return qry.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
