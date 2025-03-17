package com.rmachnik.drugs.application;

import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrugApplicationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DrugApplicationRepository repository;


    @InjectMocks
    private DrugApplicationService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "fdaUrl", "https://api.fda.gov");
    }

    @Test
    void testSearchDrugs() {
        String manufacturer = "Renew Pharmaceuticals";
        String brand = "INDOCYANINE GREEN";
        int limit = 10;
        int skip = 0;
        URI url = URI.create("https://api.fda.gov/drug/drugsfda.json?search=manufacturer_name:Renew%20Pharmaceuticals+brand_name:INDOCYANINE%20GREEN&limit=10&skip=0");

        when(restTemplate.getForEntity(url, String.class)).thenReturn(ResponseEntity.ok().body(
                """
                        {
                          "meta": {
                            "disclaimer": "Do not rely on openFDA to make decisions regarding medical care. While we make every effort to ensure that data is accurate, you should assume all results are unvalidated. We may limit or otherwise restrict your access to the API in line with our Terms of Service.",
                            "terms": "https://open.fda.gov/terms/",
                            "license": "https://open.fda.gov/license/",
                            "last_updated": "2025-03-11",
                            "results": {
                              "skip": 0,
                              "limit": 1,
                              "total": 4011
                            }
                          },
                          "results": [
                            {
                              "submissions": [
                                {
                                  "submission_type": "ORIG",
                                  "submission_number": "1",
                                  "submission_status": "AP",
                                  "submission_status_date": "20030929"
                                }
                              ],
                              "application_number": "ANDA076011",
                              "sponsor_name": "PERRIGO PHARMA INTL",
                              "openfda": {
                                "application_number": [
                                  "ANDA076011"
                                ],
                                "brand_name": [
                                  "GOOD SENSE ALLERGY RELIEF",
                                  "ALLERGY RELIEF",
                                  "DG HEALTH ALLERGY RELIEF",
                                  "CAREONE ALLERGY RELIEF",
                                  "SIGNATURE CARE ALLERGY RELIEF",
                                  "BASIC CARE ALLERGY RELIEF",
                                  "NON DROWSY ALLERGY RELIEF",
                                  "TOPCARE ALLERGY RELIEF",
                                  "LORATADINE"
                                ],
                                "generic_name": [
                                  "LORATADINE"
                                ],
                                "manufacturer_name": [
                                  "L. Perrigo Company",
                                  "Kroger Company",
                                  "Dolgencorp, LLC",
                                  "American Sales Company",
                                  "Safeway",
                                  "Amazon.com Services LLC",
                                  "Meijer Distribution Inc",
                                  "Topco Associates LLC",
                                  "Rite Aid Corporation"
                                ],
                                "product_ndc": [
                                  "0113-9755",
                                  "30142-446",
                                  "55910-594",
                                  "41520-510",
                                  "21130-452",
                                  "72288-451",
                                  "72288-200",
                                  "0113-1191",
                                  "41250-890",
                                  "21130-301",
                                  "36800-853",
                                  "11822-0558",
                                  "11822-0919",
                                  "55910-970"
                                ],
                                "product_type": [
                                  "HUMAN OTC DRUG"
                                ],
                                "route": [
                                  "ORAL"
                                ],
                                "substance_name": [
                                  "LORATADINE"
                                ],
                                "rxcui": [
                                  "311373"
                                ],
                                "spl_id": [
                                  "3ef968b8-6732-4c80-b385-f471ed407817",
                                  "7259416e-3b4f-40aa-8fec-7072b7077d2d",
                                  "c18c40ae-19c8-4699-842a-43a5c4c236d5",
                                  "bb4f118d-ddd4-407a-9a07-0660c6150ca6",
                                  "62dd431d-c820-4611-803a-720b4dbf720d",
                                  "839adea8-fd66-4f26-ac44-8ef848636b58",
                                  "c22c9880-ed0e-48a7-a30e-41f0aed5a212",
                                  "288efba1-b795-4272-a8b6-259c70be2f40",
                                  "a2c799c3-e087-4f7c-a490-c6f34214fa30",
                                  "93c52680-9876-418f-86e6-6825909b2631",
                                  "4bd57bff-c68b-497f-801d-e60d9c7b8390",
                                  "63e2370f-747b-489f-95e9-e000aee12ae1",
                                  "754e27cd-8990-46b7-9325-16e1765c0807",
                                  "e6b05fb3-9c47-4086-b8a0-c539aae087f5"
                                ],
                                "spl_set_id": [
                                  "3ef968b8-6732-4c80-b385-f471ed407817",
                                  "745d48f2-504d-4e3d-abf8-768d201cf814",
                                  "c18c40ae-19c8-4699-842a-43a5c4c236d5",
                                  "fdacfd88-eb85-4a33-9863-c6ff484fea4e",
                                  "0a4b8eef-762e-42cc-84ee-0018a9a3be58",
                                  "76e35b3f-3382-4e14-a25f-5544c75ff7c8",
                                  "c22c9880-ed0e-48a7-a30e-41f0aed5a212",
                                  "a8f97128-3af8-493d-8e66-a1e07f594131",
                                  "4762c96e-d562-4f74-a275-8cba597987f9",
                                  "8ad57dc9-36d4-4acf-8b32-81cae3382f9b",
                                  "32f4e1d1-c830-4752-8251-bcc233bf03f6",
                                  "7b804f66-9323-4119-a856-1e6f08280781",
                                  "004dd408-e3ff-4ba6-b859-beebe364f0e1",
                                  "04258018-3d57-46d7-9595-04786da3f51a"
                                ],
                                "package_ndc": [
                                  "0113-9755-65",
                                  "30142-446-65",
                                  "55910-594-46",
                                  "55910-594-65",
                                  "41520-510-65",
                                  "21130-452-65",
                                  "72288-451-65",
                                  "72288-200-65",
                                  "0113-1191-65",
                                  "41250-890-65",
                                  "21130-301-65",
                                  "36800-853-65",
                                  "11822-0558-0",
                                  "11822-0919-0",
                                  "55910-970-46"
                                ],
                                "unii": [
                                  "7AJO3BO7QN"
                                ]
                              },
                              "products": [
                                {
                                  "product_number": "001",
                                  "reference_drug": "No",
                                  "brand_name": "LORATADINE",
                                  "active_ingredients": [
                                    {
                                      "name": "LORATADINE",
                                      "strength": "10MG"
                                    }
                                  ],
                                  "reference_standard": "No",
                                  "dosage_form": "TABLET, ORALLY DISINTEGRATING",
                                  "route": "ORAL",
                                  "marketing_status": "Over-the-counter"
                                }
                              ]
                            }
                          ]
                        }
                        """
        ));

        List<DrugApplication> drugApplications = service.searchDrugs(manufacturer, brand, limit, skip);

        assertNotNull(drugApplications);
        assertEquals(1, drugApplications.size());
        DrugApplication drug = drugApplications.getFirst();
        assertEquals("ANDA076011", drug.getApplicationNumber());
        assertEquals("L. Perrigo Company", drug.getManufacturerName());
        assertEquals("LORATADINE", drug.getSubstanceName());
        assertEquals(14, drug.getProductNumbers().size());
    }

    @Test
    void testGetAllStoredApplications() {
        Pageable pageable = PageRequest.of(0, 10);
        List<DrugApplication> drugApplications = List.of(
                new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"))
        );
        Page<DrugApplication> page = new PageImpl<>(drugApplications);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<DrugApplication> result = service.getAllStoredApplications(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        DrugApplication drug = result.getContent().getFirst();
        assertEquals("ANDA040811", drug.getApplicationNumber());
        assertEquals("Renew Pharmaceuticals", drug.getManufacturerName());
        assertEquals("INDOCYANINE GREEN", drug.getSubstanceName());
        assertEquals(2, drug.getProductNumbers().size());
    }
}