package com.teatro.dawi.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teatro.dawi.model.Cliente;
import com.teatro.dawi.model.Evento;
import com.teatro.dawi.model.Funcion;
import com.teatro.dawi.model.Ticket;
import com.teatro.dawi.repository.IAreaRepository;
import com.teatro.dawi.repository.ICategoriaRepository;
import com.teatro.dawi.repository.IClienteRepository;
import com.teatro.dawi.repository.IEventoRepository;
import com.teatro.dawi.repository.IFuncionRepository;
import com.teatro.dawi.repository.ITicketRepository;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class MenuController {
	
	@Autowired
	private ICategoriaRepository repoCat;
	
	@Autowired
	private IClienteRepository repoCli;
	
	@Autowired
	private IFuncionRepository repoFunc;
	
	@Autowired
	private IEventoRepository repoEven;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired 
	private IAreaRepository repoArea;
	
	@Autowired 
	private ITicketRepository repoTicket;
	
	@GetMapping("/cargarIndex")
	public String mostrarIndex(Model model) {
		model.addAttribute("lstEventos", repoEven.findAll());
		//No olvidar
		return "listadoEventos";
	}
	
	@GetMapping("/cargarRegUsu")
	public String cargarRegUsu(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "registroUsuario";
	}
	
	@GetMapping("cargarRealizarEvento")
	public String mostrarRealizarEvento(Model model) {
		model.addAttribute("lstCategorias", repoCat.findAll());
		model.addAttribute("evento", new Evento());
		return "registrarEvento";
	}
	
	@GetMapping("/login")
	public String abrirPagLogin() {
		return "login";
	}
	@GetMapping("/detalleEvento")
	public String abrirDetalleEvento() {
		return "detalleEvento";  
	}

	
	
	
	
	
	// listar funciones
	@GetMapping("/registroFuncion")
	public String cargarFunc(Model model) {
		model.addAttribute("lstFunciones", repoFunc.findAll());
		model.addAttribute("lstEventos", repoEven.findAll());
		model.addAttribute("funcion", new Funcion());
		
		return "registroFuncion";
	}
	
	// registrar funciones
	@PostMapping("/registroFuncion")
	public String registroFuncion(@ModelAttribute Funcion funcion, Model model) {
		try {
			repoFunc.save(funcion);
			model.addAttribute("mensaje", "REGISTRO OK");
		} catch (Exception e) {
			model.addAttribute("mensaje", "ERROR AL REGISTRAR");
		}
		
		return "redirect:/registroFuncion";
		
	}
	
	/*@PostMapping("/registrarEvento")
	public String registrarEvento(@ModelAttribute Evento evento, Model model) {
		model.addAttribute("lstCategorias", repoCat.findAll());
		try {
			repoEven.save(evento);
			model.addAttribute("mensaje", "REGISTRO OK");
		} catch (Exception e) {
			model.addAttribute("mensaje", "ERROR AL REGISTRAR");
		}
		
		return "redirect:/registroFuncion";
	}*/
	
	@PostMapping("/registrarEvento")
    public String registrarEvento(@ModelAttribute Evento evento, Model model,
            @RequestParam("btnOpcion") String opcion,
            RedirectAttributes atributos) {
        if(opcion.equals("fil")) {
            atributos.addAttribute("evento", evento);
            return "redirect:/reporteCatEvento";
        }
        model.addAttribute("lstCategorias", repoCat.findAll());
        try {
            repoEven.save(evento);
            model.addAttribute("mensaje", "REGISTRO OK");
        } catch (Exception e) {
            model.addAttribute("mensaje", "ERROR AL REGISTRAR");
        }
        return "redirect:/registroFuncion";
    }
	
	@GetMapping("/editar/{idfuncion}")
    public String editar(@PathVariable int idfuncion, Model model) {
        // acciones..
        // Producto p = repoProd.getById(id_prod);
        Funcion f = repoFunc.findById(idfuncion).get();

        // enviamos el obj Producto a la pagina
        model.addAttribute("funcion", f);

        // agregamos los listados
        model.addAttribute("lstFunciones", repoFunc.findAll());
		model.addAttribute("lstEventos", repoEven.findAll());
        // salida
        return "registroFuncion";
    }
	
	@GetMapping("/eliminar/{idfuncion}")
    public String eliminar(@PathVariable int idfuncion, Model model) {
        repoFunc.deleteById(idfuncion);

        model.addAttribute("funcion", new Funcion());

        model.addAttribute("lstFunciones", repoFunc.findAll());
		model.addAttribute("lstEventos", repoEven.findAll());

        // salida
        return "registroFuncion";
    }
	
	
	@PostMapping("/login")
	public String validarAcceso(
			@RequestParam("txtUsuario") String username, 
			@RequestParam("txtClave") String clave,
			Model model) {
		
		Cliente c = repoCli.findByUsernameAndClave(username, clave);
		
		if (c != null) {
			model.addAttribute("mensaje", "Bienvenido: " + c.getNomcli());
			model.addAttribute("cssmensaje", "alert alert-success");
			 return "redirect:/cargarIndex";
		} else {
			model.addAttribute("mensaje", "Usuario o clave incorrecto");
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		
		return "login";
	}
	
	@PostMapping("/registroUsuario")
	public String registroUsuario(@ModelAttribute Cliente cliente, Model model) {
		System.out.println(cliente);
		try {
			repoCli.save(cliente);
			model.addAttribute("mensaje", "Grabacion OK!");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al grabar");
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		return "registroUsuario";
	}
	
	@GetMapping("/reporteFuncion")
    public void reporteFuncion(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "inline;");
        response.setContentType("application/pdf");
        try {
            String ru = resourceLoader.getResource("classpath:reporteFuncion.jasper").getURI().getPath();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ru, null, dataSource.getConnection());
            OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/reporteCatEvento")
    public void reporteEvento(HttpServletResponse response,
            @ModelAttribute Evento evento) {
        response.setHeader("Content-Disposition", "inline;");
        response.setContentType("application/pdf");
        try {
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("categoria", "1");
            String ru = resourceLoader.getResource("classpath:reporteCatEvento.jasper").getURI().getPath();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ru, parametros, dataSource.getConnection());
            OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
	
	//METODO BUSCAR QUE ENVIARA OBJETO A LA VISTA DETALLEvento
	
	@GetMapping ("/detalleEvento/{idevento}")
	public String detalleEvento(@PathVariable int idevento, Model model) {
			
			List<Funcion> funcion = repoFunc.findByIdeventoEquals(idevento);
			
	        Evento evento = repoEven.findById(idevento).get();

			model.addAttribute("lstArea", repoArea.findAll());
			model.addAttribute("lstFunciones", funcion);
			model.addAttribute("evento", evento);
			
			
			return "detalleEvento";
		}
	
	@GetMapping("/comprarTicket/{idevento}")
	public String comprarTicket(@PathVariable int idevento, Model model) {
		
		model.addAttribute("evento", repoEven.findById(idevento));
		model.addAttribute("lstFuncion", repoFunc.findByIdeventoEquals(idevento));
		model.addAttribute("lstArea", repoArea.findAll());
		model.addAttribute("lstClientes", repoCli.findAll());
		
        model.addAttribute("ticket", new Ticket());

		
		return "ComprarTicket";
	}
	
	@PostMapping("/comprarTicket")
	public String comprarTicket(@ModelAttribute Ticket ticket, Model model) {
		
		try {
			repoTicket.save(ticket);
			model.addAttribute("mensaje", "Grabacion OK!");
			model.addAttribute("cssmensaje", "alert alert-success");
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al grabar");
			model.addAttribute("cssmensaje", "alert alert-danger");
		}
		return "ComprarTicket";
	}
	
	
	
	

}
