package de.lechner.cbudget.rendite;

import de.lechner.cbudget.apicall.APIcall;
import de.lechner.cbudget.entities.Anlage;
import de.lechner.cbudget.entities.Konto;
import de.lechner.cbudget.entities.Rendite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig
class ComputeRenditeTest {

    @Mock
    private APIcall apicall;

    @InjectMocks
    private ComputeRendite computeRendite;

    private List<Anlage> testAnlagen;
    private List<Konto> testKonten;
    private Rendite testRendite;

    @BeforeEach
    void setUp() {
        // Test-Anlagen erstellen
        Anlage anlage1 = createAnlage("TestAnlage1", "Y", 1);
        Anlage anlage2 = createAnlage("P2p", "Y", 2);
        Anlage anlage3 = createAnlage("NoRendite", "N", 3);

        testAnlagen = Arrays.asList(anlage1, anlage2, anlage3);

        // Test-Konten erstellen
        Konto konto1 = createKonto(1, "TestKonto1", "TestAnlage1", null);
        Konto konto2 = createKonto(2, "TestKonto2", "P2p", 3);
        Konto konto3 = createKonto(3, "TestKonto3", "NoRendite", null);

        testKonten = Arrays.asList(konto1, konto2, konto3);

        // Test-Rendite erstellen
        testRendite = createRendite(1, 0, 1000.0);
    }

    @Test
    void testRendite_erfolgreicherDurchlauf() {
        // Given
        when(apicall.getAllAnalgen()).thenReturn(testAnlagen);
        when(apicall.getAllKonten()).thenReturn(testKonten);
        when(apicall.getRenditeByDateAndName(anyInt(), anyString())).thenReturn(null);
        when(apicall.getAktKontostand(anyInt(), anyString())).thenReturn("1000.0");
        when(apicall.getErtragWithRuleID(anyInt(), anyString(), anyString(), anyInt())).thenReturn("50.0");
        when(apicall.getErtragWithRuleID(anyString(), anyString(), anyInt())).thenReturn("30.0");

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, times(1)).getAllAnalgen();
        verify(apicall, times(1)).getAllKonten();
        verify(apicall, atLeastOnce()).getRenditeByDateAndName(anyInt(), anyString());
        verify(apicall, atLeastOnce()).insertRendite(anyInt(), anyDouble(), anyString(), anyDouble(), isNull());
    }

    @Test
    void testRendite_mitDirtyRendite() {
        // Given
        Rendite dirtyRendite = createRendite(1, 1, 1000.0);
        when(apicall.getAllAnalgen()).thenReturn(Arrays.asList(testAnlagen.get(0)));
        when(apicall.getAllKonten()).thenReturn(Arrays.asList(testKonten.get(0)));
        when(apicall.getRenditeByDateAndName(anyInt(), anyString())).thenReturn(dirtyRendite);
        when(apicall.getAktKontostand(anyInt(), anyString())).thenReturn("1000.0");
        when(apicall.getErtragWithRuleID(anyInt(), anyString(), anyString(), anyInt())).thenReturn("50.0");

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, atLeastOnce()).insertRendite(anyInt(), anyDouble(), anyString(), anyDouble(), eq(1));
    }

    @Test
    void testRendite_anlageOhneRenditeBerechnung() {
        // Given
        when(apicall.getAllAnalgen()).thenReturn(Arrays.asList(testAnlagen.get(2))); // NoRendite Anlage
        when(apicall.getAllKonten()).thenReturn(Arrays.asList(testKonten.get(2)));

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, never()).getRenditeByDateAndName(anyInt(), anyString());
        verify(apicall, never()).insertRendite(anyInt(), anyDouble(), anyString(), anyDouble(), any());
    }

    @Test
    void testRendite_mitNullKontostand() {
        // Given
        when(apicall.getAllAnalgen()).thenReturn(Arrays.asList(testAnlagen.get(0)));
        when(apicall.getAllKonten()).thenReturn(Arrays.asList(testKonten.get(0)));
        when(apicall.getRenditeByDateAndName(anyInt(), anyString())).thenReturn(null);
        when(apicall.getAktKontostand(anyInt(), anyString())).thenReturn("0.0");
        when(apicall.getErtragWithRuleID(anyInt(), anyString(), anyString(), anyInt())).thenReturn("50.0");

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, atLeastOnce()).getAktKontostand(anyInt(), anyString());
    }

    @Test
    void testRendite_p2pSpezialbehandlung() {
        // Given
        when(apicall.getAllAnalgen()).thenReturn(Arrays.asList(testAnlagen.get(1))); // P2p Anlage
        when(apicall.getAllKonten()).thenReturn(Arrays.asList(testKonten.get(1))); // P2p Konto
        when(apicall.getRenditeByDateAndName(anyInt(), anyString())).thenReturn(null);
        when(apicall.getAktKontostand(anyInt(), anyString())).thenReturn("1000.0");
        when(apicall.getErtragWithRuleID(anyString(), anyString(), anyInt())).thenReturn("10.0");

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, atLeastOnce()).getErtragWithRuleID(anyString(), anyString(), eq(3));
    }

    @Test
    void testRendite_bereitsBerechnetUndSauber() {
        // Given
        when(apicall.getAllAnalgen()).thenReturn(Arrays.asList(testAnlagen.get(0)));
        when(apicall.getAllKonten()).thenReturn(Arrays.asList(testKonten.get(0)));
        when(apicall.getRenditeByDateAndName(anyInt(), anyString())).thenReturn(testRendite);

        // When
        computeRendite.rendite();

        // Then
        verify(apicall, never()).insertRendite(anyInt(), anyDouble(), anyString(), anyDouble(), any());
    }

    // Helper-Methoden für bessere Lesbarkeit
    private Anlage createAnlage(String name, String rendite, Integer ruleId) {
        Anlage anlage = new Anlage();
        anlage.setName(name);
        anlage.setRendite(rendite);
        anlage.setRule_id(ruleId);
        return anlage;
    }

    private Konto createKonto(Integer id, String kontoname, String mode, Integer ruleId) {
        Konto konto = new Konto();
        konto.setId(id);
        konto.setKontoname(kontoname);
        konto.setMode(mode);
        konto.setRule_id(ruleId);
        return konto;
    }

    private Rendite createRendite(Integer id, Integer dirty, Double amount) {
        Rendite rendite = new Rendite();
        rendite.setId(id);
        rendite.setDirty(dirty);
        rendite.setAmount(amount);
        return rendite;
    }
}