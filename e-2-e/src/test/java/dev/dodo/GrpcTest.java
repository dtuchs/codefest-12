package dev.dodo;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.Empty;
import dev.dodo.geoservice.GeoInfoRequest;
import dev.dodo.geoservice.GeoInfoResponse;
import dev.dodo.geoservice.GeoServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GrpcTest {

    private Channel ch = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .build();

    private GeoServiceGrpc.GeoServiceBlockingStub stub =
            GeoServiceGrpc.newBlockingStub(ch).withInterceptors(new AllureGrpc());

    @Test
    void unaryCallTest() {
        GeoInfoResponse expected = GeoInfoResponse.newBuilder()
                .setGeoCode(84)
                .setName("VN")
                .build();

        GeoInfoResponse response = stub.getGeoInfo(GeoInfoRequest.newBuilder()
                .setGeoCode(84)
                .build());

        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id_", "memoizedHashCode")
                .isEqualTo(expected);
    }

    @Test
    void streamingTest() {
        Iterator<GeoInfoResponse> allGeo = stub.getAllGeo(Empty.getDefaultInstance());
        ImmutableList<GeoInfoResponse> infoResponses = ImmutableList.copyOf(allGeo);
        assertThat(infoResponses).hasSize(3);
    }

}
