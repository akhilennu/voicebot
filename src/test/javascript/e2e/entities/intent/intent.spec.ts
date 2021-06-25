import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import IntentComponentsPage from './intent.page-object';
import IntentUpdatePage from './intent-update.page-object';
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

describe('Intent e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let intentComponentsPage: IntentComponentsPage;
  let intentUpdatePage: IntentUpdatePage;
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
    intentComponentsPage = new IntentComponentsPage();
    intentComponentsPage = await intentComponentsPage.goToPage(navBarPage);
  });

  it('should load Intents', async () => {
    expect(await intentComponentsPage.title.getText()).to.match(/Intents/);
    expect(await intentComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Intents', async () => {
    const beforeRecordsCount = (await isVisible(intentComponentsPage.noRecords)) ? 0 : await getRecordsCount(intentComponentsPage.table);
    intentUpdatePage = await intentComponentsPage.goToCreateIntent();
    await intentUpdatePage.enterData();
    expect(await isVisible(intentUpdatePage.saveButton)).to.be.false;

    expect(await intentComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(intentComponentsPage.table);
    await waitUntilCount(intentComponentsPage.records, beforeRecordsCount + 1);
    expect(await intentComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await intentComponentsPage.deleteIntent();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(intentComponentsPage.records, beforeRecordsCount);
      expect(await intentComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(intentComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
