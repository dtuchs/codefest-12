package dev.dodo.geoservice.service;

import com.google.protobuf.Empty;
import dev.dodo.geoservice.GeoInfoRequest;
import dev.dodo.geoservice.GeoInfoResponse;
import dev.dodo.geoservice.GeoServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

import static java.util.UUID.randomUUID;

@GrpcService
public class GeoService extends GeoServiceGrpc.GeoServiceImplBase {

    private Map<Integer, GeoInfoResponse> availableGeo = Map.of(
            84, GeoInfoResponse.newBuilder()
                    .setGeoCode(84)
                    .setName("VN")
                    .build(),
            234, GeoInfoResponse.newBuilder()
                    .setGeoCode(234)
                    .setName("NG")
                    .build(),
            49, GeoInfoResponse.newBuilder()
                    .setGeoCode(49)
                    .setName("DE")
                    .build()
    );

    @Override
    public void getGeoInfo(GeoInfoRequest request, StreamObserver<GeoInfoResponse> responseObserver) {
        GeoInfoResponse response = availableGeo.get(request.getGeoCode());
        responseObserver.onNext(response.toBuilder().setId(randomUUID().toString()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllGeo(Empty request, StreamObserver<GeoInfoResponse> responseObserver) {
        availableGeo.values().stream()
                .peek(r -> sleep())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
