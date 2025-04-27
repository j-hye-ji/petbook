//package com.hyeji.petbook.service;
//
//import com.hyeji.petbook.entity.Store;
//import com.hyeji.petbook.document.StoreDocument;
//import com.hyeji.petbook.repository.StoreRepository;
//import com.hyeji.petbook.repository.StoreSearchRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class StoreSearchService {
//
//    private final StoreRepository storeRepository;
//    private final StoreSearchRepository storeSearchRepository;
//
//    /* MySQL 데이터 -> ElasticSearch로 동기화 */
//    public void synchronizeMysqlToES() {
//        List<Store> stores = storeRepository.findAll();
//        List<StoreDocument> storeDocuments = stores.stream()
//                .map(store -> new StoreDocument(
//                store.getId().toString(),
//                store.getStoreName(),
//                store.getDescription(),
//                store.getLocation()
//        )).collect(Collectors.toList());
//
//        storeSearchRepository.saveAll(storeDocuments);
//    }
//
//    // ElasticSearch로 검색 (키워드만으로도 가능)
//    public List<StoreDocument> searchStores(String keyword) {
//        return storeSearchRepository.findByStoreNameContaining(keyword);
//    }
//}
