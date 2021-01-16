package es.ewic.backend.service.sellerService;

import java.util.List;

import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.IncorrectPasswordException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface SellerService {

	List<Seller> getSellers();

	Seller getSellerById(int idSeller) throws InstanceNotFoundException;

	Seller getSellerByLoginName(String loginName) throws InstanceNotFoundException;

	Seller saveOrUpdateSeller(Seller seller) throws DuplicateInstanceException;

	Seller login(String loginName, String password) throws InstanceNotFoundException, IncorrectPasswordException;

	void changePassword(int idSeller, String newPwd, String oldPwd)
			throws InstanceNotFoundException, IncorrectPasswordException;

	void deleteSeller(int idSeller) throws InstanceNotFoundException;

}
