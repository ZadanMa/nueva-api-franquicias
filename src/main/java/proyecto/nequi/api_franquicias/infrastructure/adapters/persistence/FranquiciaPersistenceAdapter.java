package proyecto.nequi.api_franquicias.infrastructure.adapters.persistence;

import org.springframework.stereotype.Component;
import proyecto.nequi.api_franquicias.domain.model.*;
import proyecto.nequi.api_franquicias.domain.spi.FranquiciaPersistencePort;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.FranquiciaWithDetailsMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.mapper.ProductoMapper;
import proyecto.nequi.api_franquicias.infrastructure.adapters.persistence.repository.FranquiciaRepository;

import reactor.core.publisher.Mono;

@Component
public class FranquiciaPersistenceAdapter implements FranquiciaPersistencePort {

    private final FranquiciaRepository repository;
    private final FranquiciaMapper mapper;



    public FranquiciaPersistenceAdapter(FranquiciaRepository repository, FranquiciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        return repository.save(mapper.toEntity(franquicia))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Mono<Franquicia> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }



    @Override
    public Mono<Franquicia> updateName(Long id, String newName) {
        return repository.findById(id)
                .flatMap(entity -> {
                    entity.setNombre(newName);
                    return repository.save(entity);
                })
                .map(mapper::toModel);
    }


//    @Override
//    public Mono<FranquiciaWithDetails> findWithDetailsById(Long id) {
//        return repository.findFranquiciaDetailsById(id)
//                .collectList()
//                .filter(list -> !list.isEmpty())
//                .map(this::mapToDomainModel)
//                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND)))
//                .onErrorMap(e -> new TechnicalException(TechnicalMessage.FAILED_TO_RETRIEVE_ENTITY));
//    }
//
//    private FranquiciaWithDetails mapToDomainModel(List<Map<String, Object>> rawDataList) {
//        // Extraer datos de la franquicia
//        Map<String, Object> firstRow = rawDataList.get(0);
//        FranquiciaEntity franquiciaEntity = new FranquiciaEntity((Long)firstRow.get("franquicia_id"), (String) firstRow.get("franquicia_nombre"));
//        Franquicia franquiciaBase = mapper.toModel(franquiciaEntity);
//
//        // Verificar si la franquicia existe
//        if (franquiciaBase == null) {
//            throw new BusinessException(TechnicalMessage.FRANQUICIA_NOT_FOUND);
//        }
//        // Verificar si la franquicia tiene sucursales
//        if (rawDataList.isEmpty()) {
//            throw new BusinessException(TechnicalMessage.FRANQUICIA_WITHOUT_BRANCHES);
//        }
//
//        // Agrupar sucursales y productos
//        Map<Long, SucursalWithProductos> sucursalMap = new HashMap<>();
//        rawDataList.forEach(row -> {
//            Long sucursalId = (Long) row.get("sucursal_id");
//            if (sucursalId != null) {
//                // Mapear sucursal
//                SucursalEntity sucursalEntity = new SucursalEntity(
//                        sucursalId,
//                        (String) row.get("sucursal_nombre"),
//                        franquiciaBase.id()
//                );
//                SucursalWithProductos sucursal = sucursalMapper.toModel(sucursalEntity);
//
//                // Mapear producto (si existe)
//                Long productoId = (Long) row.get("producto_id");
//                if (productoId != null) {
//                    ProductoEntity productoEntity = new ProductoEntity(
//                            productoId,
//                            (String) row.get("producto_nombre"),
//                            (Integer) row.get("stock"),
//                            sucursalId
//                    );
//                    Producto producto = productoMapper.toModel(productoEntity);
//                    sucursal.productos().add(producto);
//                }
//
//                // Agregar sucursal al mapa
//                sucursalMap.put(sucursalId, sucursal);
//            }
//        });
//
//        return new FranquiciaWithDetails(
//                franquiciaBase.id(),
//                franquiciaBase.nombre(),
//                new ArrayList<>(sucursalMap.values())
//        );
//    }
}