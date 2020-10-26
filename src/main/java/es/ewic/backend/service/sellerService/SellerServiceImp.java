package es.ewic.backend.service.sellerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.model.seller.SellerDao;
import es.ewic.backend.modelutil.PasswordEncrypter.PasswordEncrypter;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.IncorrectPasswordException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Service("sellerService")
@Transactional
public class SellerServiceImp implements SellerService {

	@Autowired
	private SellerDao sellerDao;

	@Override
	@Transactional(readOnly = true)
	public List<Seller> getSellers() {
		return sellerDao.getAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Seller getSellerById(int idSeller) throws InstanceNotFoundException {
		return sellerDao.find(idSeller);
	}

	@Override
	@Transactional(readOnly = true)
	public Seller getSellerByLoginName(String loginName) throws InstanceNotFoundException {
		return sellerDao.findByLoginName(loginName);
	}

	@Override
	public Seller saveOrUpdateSeller(Seller seller) throws DuplicateInstanceException {
		try {
			Seller s = sellerDao.find(seller.getIdSeller());
			if (!s.getLoginName().equals(seller.getLoginName())) {
				checkSellerDuplicate(seller.getLoginName());
			}
			s.setLoginName(seller.getLoginName());
			s.setFirstName(seller.getFirstName());
			s.setLastName(seller.getLastName());
			s.setEmail(seller.getEmail());
			return s;
		} catch (InstanceNotFoundException e) {
			checkSellerDuplicate(seller.getLoginName());

			seller.setPassword(PasswordEncrypter.crypt(seller.getPassword()));
			sellerDao.save(seller);
			return seller;
		}
	}

	private void checkSellerDuplicate(String loginName) throws DuplicateInstanceException {

		try {
			sellerDao.findByLoginName(loginName);
			throw new DuplicateInstanceException(loginName, Seller.class.getSimpleName());
		} catch (InstanceNotFoundException e) {
			// no duplicate
		}

	}

	@Override
	public Seller login(String loginName, String password)
			throws InstanceNotFoundException, IncorrectPasswordException {
		Seller seller = sellerDao.findByLoginName(loginName);
		boolean pwdCorrect = PasswordEncrypter.isClearPasswordCorrect(password, seller.getPassword());
		if (pwdCorrect) {
			return seller;
		} else {
			throw new IncorrectPasswordException(loginName, Seller.class.getSimpleName());
		}
	}

	@Override
	public void changePassword(int idSeller, String newPwd, String oldPwd)
			throws InstanceNotFoundException, IncorrectPasswordException {
		Seller seller = sellerDao.find(idSeller);
		if (PasswordEncrypter.isClearPasswordCorrect(oldPwd, seller.getPassword())) {
			seller.setPassword(PasswordEncrypter.crypt(newPwd));
		} else {
			throw new IncorrectPasswordException(seller.getLoginName(), Seller.class.getSimpleName());
		}

	}

	@Override
	public void deleteSeller(int idSeller) throws InstanceNotFoundException {
		sellerDao.remove(idSeller);

	}

}
