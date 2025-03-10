package com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfiguracionPagoService {
    @Autowired
    private ConfiguracionPagoRepository configuracionPagoRepository;

    public double obtenerPrecioPorTipo(String tipoPago) {
        return configuracionPagoRepository.findByTipoPago(tipoPago)
                .map(ConfiguracionPago::getMonto)
                .orElseThrow(() -> new NotFoundException("No se encontró configuración para el pago de " + tipoPago));
    }

    @Transactional
    public List<ConfiguracionPago> findPagos(){
        return configuracionPagoRepository.findAll();
    }

    @Transactional
    public String actualizarPrecio(String tipoPago, double nuevoMonto) {
        ConfiguracionPago config = configuracionPagoRepository.findByTipoPago(tipoPago)
                .orElseThrow(() -> new NotFoundException("No se encontró configuración para el pago de " + tipoPago));
        config.setMonto(nuevoMonto);
        configuracionPagoRepository.save(config);
        return "Precio de " + tipoPago + " actualizado a $" + nuevoMonto;
    }
}

