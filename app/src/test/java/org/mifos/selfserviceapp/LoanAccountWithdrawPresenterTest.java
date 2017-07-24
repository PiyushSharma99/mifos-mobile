package org.mifos.selfserviceapp;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithdraw;
import org.mifos.selfserviceapp.presenters.LoanAccountWithdrawPresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountWithdrawView;
import org.mifos.selfserviceapp.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.ResponseBody;
import rx.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dilpreet on 24/7/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanAccountWithdrawPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    LoanAccountWithdrawView view;

    @Mock
    LoanWithdraw loanWithdraw;

    @Mock
    ResponseBody mockedResponseBody;

    private LoanAccountWithdrawPresenter presenter;
    private long loanId = 1;

    @Before
    public void setUp() {
        presenter = new LoanAccountWithdrawPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }

    @Test
    public void testWithdrawLoanAccount() {
        when(dataManager.withdrawLoanAccount(loanId, loanWithdraw)).thenReturn(Observable.
                just(mockedResponseBody));

        presenter.withdrawLoanAccount(loanId, loanWithdraw);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountWithdrawSuccess();
        verify(view, never()).showLoanAccountWithdrawError(context.getString(R.string.
                error_loan_account_withdraw));
    }

    @Test
    public void testWithdrawLoanAccountFail() {
        when(dataManager.withdrawLoanAccount(loanId, loanWithdraw)).thenReturn(Observable.
                <ResponseBody>error(new RuntimeException()));

        presenter.withdrawLoanAccount(loanId, loanWithdraw);

        verify(view).showProgress();
        verify(view).hideProgress();
        verify(view).showLoanAccountWithdrawError(context.getString(R.string.
                error_loan_account_withdraw));
        verify(view, never()).showLoanAccountWithdrawSuccess();
    }

}
