package de.boot.template.web.model;



import de.boot.template.web.model.profiles.Profile;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by tim on 08.10.2015.
 */

@Entity(name = "basis_data")
public class BasisData extends AbstractModel {

        @Id
        @GeneratedValue(strategy = GenerationType.TABLE)
        private Long id;

        private String beginMs;
        private String firstDiagnosis;
        private String continuousForm;
        private String continuousFormSince;
        private int basisDataPercentageFilled;

        /*
        Begleiterkrankung
         */
        private String comorbidity;

        /**
         * @param profile
         * @param comorbidity
         * @param beginMS
         * @param firstDiagnosis
         * @param continuousForm
         * @param continuousFormSince
         */
        public BasisData(Profile profile,
                         String comorbidity,
                         String beginMS,
                         String firstDiagnosis,
                         String continuousForm,
                         String continuousFormSince) {

                this.basisDataPercentageFilled = 0;
                this.comorbidity = comorbidity;
                this.beginMs = beginMS;
                this.firstDiagnosis = firstDiagnosis;
                this.continuousForm = continuousForm;
                this.continuousFormSince = continuousFormSince;

        }

        public BasisData() {
        }

        public String getComorbidity() {
                return comorbidity;
        }

        /**
         * @param begleiterkrankungen
         */
        public void setComorbidity(String begleiterkrankungen) {
                this.comorbidity = begleiterkrankungen;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getBeginMS() {
                return beginMs;
        }

        public void setBeginMS(String beginnDerMs) {
                this.beginMs = beginnDerMs;
        }

        public String getFirstDiagnosis() {
                return firstDiagnosis;
        }

        public void setFirstDiagnosis(String erstDiagnose) {
                this.firstDiagnosis = erstDiagnose;
        }

        public String getContinuousForm() {
                return continuousForm;
        }

        public void setContinuousForm(String verlaufsform) {
                this.continuousForm = verlaufsform;
        }

        public String getContinuousFormSince() {
                return continuousFormSince;
        }

        public void setContinuousFormSince(String verlaufsformSeit) {
                this.continuousFormSince = verlaufsformSeit;
        }

        public int getBasisDataPercentageFilled() {

                return basisDataPercentageFilled;
        }

        public void setBasisDataPercentageFilled(int basisDataPercentageFilled) {

                this.basisDataPercentageFilled = basisDataPercentageFilled;
        }

        @Override
        public JSONObject toJson() {
                return null;
        }

}