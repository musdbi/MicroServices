package fr.dauphine.miageIf.msa.exemple;
import fr.dauphine.miageIf.msa.MSA.TauxChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TauxChangeRepository extends JpaRepository<TauxChange, Long>
{
    TauxChange findBySourceAndDest(String source, String dest);
}
