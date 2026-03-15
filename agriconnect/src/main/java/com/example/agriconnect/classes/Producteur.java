    package com.example.agriconnect.classes;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.OneToMany;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.List;


    @Entity
    @Getter
    @Setter
    public class Producteur extends Utilisateur {
        private String typeProduit;
        private Double surfaceExploitation;

        public List<Offre> getOffres() {
            return offres;
        }

        public void setOffres(List<Offre> offres) {
            this.offres = offres;
        }

        public String getTypeProduit() {
            return typeProduit;
        }

        public void setTypeProduit(String typeProduit) {
            this.typeProduit = typeProduit;
        }

        public Double getSurfaceExploitation() {
            return surfaceExploitation;
        }

        public void setSurfaceExploitation(Double surfaceExploitation) {
            this.surfaceExploitation = surfaceExploitation;
        }

        @OneToMany(mappedBy = "producteur", fetch = FetchType.LAZY)
        @JsonIgnoreProperties("producteur")
        private List<Offre> offres;
    }
