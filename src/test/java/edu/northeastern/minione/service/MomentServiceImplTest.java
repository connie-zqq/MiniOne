package edu.northeastern.minione.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.repository.SpaceRepository;

@RunWith(MockitoJUnitRunner.class)
public class MomentServiceImplTest {
    private static final Space SPACE1 = new Space(1L, "some-name-1", "some-description-1");
    private static final Space SPACE2 = new Space(2L, "some-name-2", "some-description-2");

    @Mock
    private SpaceRepository spaceRepository;

    private MomentServiceImpl momentService;

    @Before
    public void setUp() {
        momentService = new MomentServiceImpl();
        momentService.spaceRepository = spaceRepository;
    }

    @Test
    public void testFindAllSpaces() {
        List<Space> expected = Arrays.asList(SPACE1, SPACE2);
        when(spaceRepository.findAll()).thenReturn(expected);
        assertThat(momentService.findAllSpaces()).containsExactlyElementsOf(expected);
    }

    @Test
    public void testFindSpaceById() {
        doReturn(Optional.of(SPACE1))
                .when(spaceRepository)
                .findById(1L);
        doReturn(Optional.empty())
                .when(spaceRepository)
                .findById(404L);
        assertThat(momentService.findSpaceById(1L)).hasValue(SPACE1);
        assertThat(momentService.findSpaceById(404L)).isEmpty();
    }

    @Test
    public void testCreateSpace() {
        // TODO: impl
    }

    @Test
    public void testEditSpace() {
        // TODO: impl
    }

    @Test
    public void deleteSpaceById() {
        // TODO: impl
    }
}
