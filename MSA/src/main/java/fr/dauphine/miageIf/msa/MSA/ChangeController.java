package fr.dauphine.miageIf.msa.MSA;

@RestController
public class ChangeController {
    @Autowired
    private Environment environment;
    @Autowired
    private TauxChangeRepository repository;
    GetMapping("/devise-change/source/{source}/dest/{dest}")
    public TauxChange retrouveTauxChange
            (@PathVariable String source, @PathVariable String dest){
        TauxChange tauxChange = repository.findBySourceAndDest(source, dest);
        return tauxChange;
    }
}