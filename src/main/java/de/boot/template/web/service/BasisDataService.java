package de.boot.template.web.service;

import com.domingosuarez.boot.autoconfigure.jade4j.JadeHelper;

import de.boot.template.web.model.BasisData;
import de.boot.template.web.model.Repositories.BasisDataRepository;
import de.boot.template.web.model.Repositories.ProfileRepository;
import de.boot.template.web.model.profiles.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by tim on 28.12.2015.
 */
@Repository
@JadeHelper("basisDataService")
public class BasisDataService {

        @Autowired
        BasisDataRepository basisDataRepository;
        @Autowired
        ProfileRepository profileRepository;

        public synchronized BasisData createBasisData(Profile profile,
                                                      String comorbidity,
                                                      String beginMS,
                                                      String firstDiagnosis,
                                                      String continuousForm,
                                                      String continuousFormSince) {
                BasisData basisData = new BasisData(profile, comorbidity, beginMS, firstDiagnosis, continuousForm, continuousFormSince);
                basisDataRepository.save(basisData);
                return basisData;
        }

        public synchronized void delete(BasisData basisData) {
                basisDataRepository.delete(basisData);
        }



        public synchronized void save(BasisData basisData) {
                basisDataRepository.save(basisData);
        }

        public synchronized BasisData getOne(Long id) {
                return basisDataRepository.getOne(id);
        }

}
