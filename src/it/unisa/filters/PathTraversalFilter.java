package it.unisa.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class PathTraversalFilter
 */
@WebFilter(filterName = "/PathTraversalFilter", urlPatterns = "/*")
public class PathTraversalFilter extends HttpFilter implements Filter {
       
	private static final long serialVersionUID = 1L;
	private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    	String path = httpRequest.getContextPath();
        String page = httpRequest.getParameter("page");

        // Verifica se il parametro "page" contiene un valore valido
        if (page != null && !page.isEmpty()) {
            // Controlla se il parametro "page" è un reindirizzamento verso file sensibili
            if (page.contains("META-INF/context.xml") || page.contains("WEB-INF/web.xml")) {
                // Se sì, forza il reindirizzamento verso un'altra pagina del sito
                httpResponse.sendRedirect(path + "/Home.jsp");
                return;
            }
        }

        // Procedi con la catena di filtri e l'esecuzione della servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Pulizia delle risorse
    }

}
