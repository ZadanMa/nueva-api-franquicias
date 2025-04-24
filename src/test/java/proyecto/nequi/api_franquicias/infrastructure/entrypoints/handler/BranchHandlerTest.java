package proyecto.nequi.api_franquicias.infrastructure.entrypoints.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import proyecto.nequi.api_franquicias.domain.api.BranchServicePort;
import proyecto.nequi.api_franquicias.domain.enums.TechnicalMessage;
import proyecto.nequi.api_franquicias.domain.exceptions.BusinessException;
import proyecto.nequi.api_franquicias.domain.exceptions.TechnicalException;
import proyecto.nequi.api_franquicias.domain.model.Branch;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.dto.BranchUpdateDTO;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.mapper.BranchDTOMapper;
import proyecto.nequi.api_franquicias.infrastructure.entrypoints.router.BranchRouter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchHandlerTest {

    @Mock
    private BranchServicePort servicePort;
    @Mock
    private BranchDTOMapper dtoMapper;
    @InjectMocks
    private BranchHandler handler;

    private WebTestClient webTestClient;
    private RouterFunction<ServerResponse> routerFunction;

    @BeforeEach
    void setUp() {
        BranchRouter branchRouter = new BranchRouter();
        routerFunction = branchRouter.sucursalRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void testRegisterBranch_Success() {
        BranchDTO requestDto = new BranchDTO(null, "Branch A", 1L);
        Branch branch = new Branch(101L, "Branch A", 1L);
        BranchDTO responseDto = new BranchDTO(101L, "Branch A", 1L);

        when(dtoMapper.toModel(requestDto)).thenReturn(branch);
        when(servicePort.registerBranch(branch)).thenReturn(Mono.just(branch));
        when(dtoMapper.toDto(branch)).thenReturn(responseDto);

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_CREATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_CREATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.name").isEqualTo("Branch A")
                .jsonPath("$.data.franchiseId").isEqualTo(1);
    }

    @Test
    void testRegisterBranch_BusinessException() {

        BranchDTO requestDto = new BranchDTO(null, "Branch A", 1L);
        Branch branch = new Branch(101L, "Branch A", 1L);

        when(dtoMapper.toModel(requestDto)).thenReturn(branch);
        when(servicePort.registerBranch(branch))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_ALREADY_EXISTS)));

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_ALREADY_EXISTS.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_ALREADY_EXISTS.getMessage());
    }
    @Test
    void testRegisterBranch_TechnicalException() {
        BranchDTO requestDto = new BranchDTO(null, "Branch A", 1L);
        Branch branch = new Branch(101L, "Branch A", 1L);
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(dtoMapper.toModel(requestDto)).thenReturn(branch);
        when(servicePort.registerBranch(branch)).thenReturn(Mono.error(technicalException));

        webTestClient.post().uri("/sucursales")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetAllBranch_Success() {
        Branch branch1 = new Branch(101L, "Branch A", 1L);
        Branch branch2 = new Branch(102L, "Branch B", 1L);
        List<Branch> sucursales = List.of(branch1, branch2);

        when(servicePort.getAllSucursales()).thenReturn(Flux.fromIterable(sucursales));
        when(dtoMapper.toDto(any(Branch.class))).thenReturn(new BranchDTO(101L, "Branch A", 1L));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                .jsonPath("$.data.length()").isEqualTo(2)
                .jsonPath("$.data[0].id").isEqualTo(101)
                .jsonPath("$.data[0].name").isEqualTo("Branch A");
    }
    @Test
    void testGetAllBranch_BusinessException() {
        when(servicePort.getAllSucursales()).thenReturn(Flux.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testGetAllBranch_TechnicalException() {
        when(servicePort.getAllSucursales()).thenReturn(Flux.error(new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR)));

        webTestClient.get().uri("/sucursales")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testGetBranchById_Success() {
        Long sucursalId = 101L;
        Branch branch = new Branch(sucursalId, "Branch A", 1L);
        BranchDTO responseDto = new BranchDTO(sucursalId, "Branch A", 1L);

        when(servicePort.getBranchById(sucursalId)).thenReturn(Mono.just(branch));
        when(dtoMapper.toDto(branch)).thenReturn(responseDto);

        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_FOUND.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.name").isEqualTo("Branch A");
    }


    @Test
    void testGetBranchById_NotFound() {
        // Arrange
        Long sucursalId = 101L;

        when(servicePort.getBranchById(sucursalId)).thenReturn(Mono.error(new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND)));

        // Act & Assert
        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testGetBranchById_TechnicalException() {
        Long sucursalId = 101L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.getBranchById(sucursalId)).thenReturn(Mono.error(technicalException));

        webTestClient.get().uri("/sucursales/{sucursalId}", sucursalId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testUpdateNameBranch_Success() {

        Long sucursalId = 101L;
        BranchUpdateDTO requestDto = new BranchUpdateDTO("Branch Nueva");
        Branch branchActualizada = new Branch(sucursalId, "Branch Nueva", 1L);
        BranchDTO responseDto = new BranchDTO(sucursalId, "Branch Nueva", 1L);

        when(servicePort.updateNameBranch(sucursalId, requestDto.newName()))
                .thenReturn(Mono.just(branchActualizada));
        when(dtoMapper.toDto(branchActualizada)).thenReturn(responseDto);


        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_UPDATED.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_UPDATED.getMessage())
                .jsonPath("$.data.id").isEqualTo(101)
                .jsonPath("$.data.name").isEqualTo("Branch Nueva");
    }

    @Test
    void testUpdateNameBranch_BusinessException() {
        Long sucursalId = 101L;
        BranchUpdateDTO requestDto = new BranchUpdateDTO("Branch Nueva");
        BusinessException businessException = new BusinessException(TechnicalMessage.SUCURSAL_NOT_FOUND);

        when(servicePort.updateNameBranch(sucursalId, requestDto.newName()))
                .thenReturn(Mono.error(businessException));

        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.SUCURSAL_NOT_FOUND.getMessage());
    }

    @Test
    void testUpdateNameBranch_TechnicalException() {
        Long sucursalId = 101L;
        BranchUpdateDTO requestDto = new BranchUpdateDTO("Branch Nueva");
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.updateNameBranch(sucursalId, requestDto.newName()))
                .thenReturn(Mono.error(technicalException));

        webTestClient.put().uri("/sucursales/{sucursalId}/nombre", sucursalId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testProductMostStockPerBranch_Success() {

        Long franquiciaId = 1L;
        Map<String, Object> productoData = Map.of(
                "producto_nombre", "Hamburguesa",
                "stock", 100,
                "sucursal_id", 101L
        );

        when(servicePort.productMostStockPerBranch(franquiciaId))
                .thenReturn(Flux.just(productoData));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCTO_FOUND.getMessage())
                .jsonPath("$.data[0].producto_nombre").isEqualTo("Hamburguesa")
                .jsonPath("$.data[0].stock").isEqualTo(100)
                .jsonPath("$.data[0].sucursal_id").isEqualTo(101);
    }

    @Test
    void testProductMostStockPerBranch_BusinessException() {
        Long franquiciaId = 1L;
        BusinessException businessException = new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND);

        when(servicePort.productMostStockPerBranch(franquiciaId)).thenReturn(Flux.error(businessException));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testProductMostStockPerBranch_TechnicalException() {
        Long franquiciaId = 1L;
        TechnicalException technicalException = new TechnicalException(TechnicalMessage.INTERNAL_SERVER_ERROR);

        when(servicePort.productMostStockPerBranch(franquiciaId)).thenReturn(Flux.error(technicalException));

        webTestClient.get().uri("/franquicias/{franquiciaId}/productos-mas-stock", franquiciaId)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.code").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getCode())
                .jsonPath("$.message").isEqualTo(TechnicalMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

}