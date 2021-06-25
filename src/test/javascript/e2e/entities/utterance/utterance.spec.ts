import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import UtteranceComponentsPage from './utterance.page-object';
import UtteranceUpdatePage from './utterance-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('Utterance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let utteranceComponentsPage: UtteranceComponentsPage;
  let utteranceUpdatePage: UtteranceUpdatePage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();
    await signInPage.username.sendKeys(username);
    await signInPage.password.sendKeys(password);
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    utteranceComponentsPage = new UtteranceComponentsPage();
    utteranceComponentsPage = await utteranceComponentsPage.goToPage(navBarPage);
  });

  it('should load Utterances', async () => {
    expect(await utteranceComponentsPage.title.getText()).to.match(/Utterances/);
    expect(await utteranceComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Utterances', async () => {
    const beforeRecordsCount = (await isVisible(utteranceComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(utteranceComponentsPage.table);
    utteranceUpdatePage = await utteranceComponentsPage.goToCreateUtterance();
    await utteranceUpdatePage.enterData();
    expect(await isVisible(utteranceUpdatePage.saveButton)).to.be.false;

    expect(await utteranceComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(utteranceComponentsPage.table);
    await waitUntilCount(utteranceComponentsPage.records, beforeRecordsCount + 1);
    expect(await utteranceComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await utteranceComponentsPage.deleteUtterance();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(utteranceComponentsPage.records, beforeRecordsCount);
      expect(await utteranceComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(utteranceComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
