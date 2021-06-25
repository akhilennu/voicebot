import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import NEREntityComponentsPage from './ner-entity.page-object';
import NEREntityUpdatePage from './ner-entity-update.page-object';
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

describe('NEREntity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nEREntityComponentsPage: NEREntityComponentsPage;
  let nEREntityUpdatePage: NEREntityUpdatePage;
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
    nEREntityComponentsPage = new NEREntityComponentsPage();
    nEREntityComponentsPage = await nEREntityComponentsPage.goToPage(navBarPage);
  });

  it('should load NEREntities', async () => {
    expect(await nEREntityComponentsPage.title.getText()).to.match(/NER Entities/);
    expect(await nEREntityComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete NEREntities', async () => {
    const beforeRecordsCount = (await isVisible(nEREntityComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(nEREntityComponentsPage.table);
    nEREntityUpdatePage = await nEREntityComponentsPage.goToCreateNEREntity();
    await nEREntityUpdatePage.enterData();
    expect(await isVisible(nEREntityUpdatePage.saveButton)).to.be.false;

    expect(await nEREntityComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(nEREntityComponentsPage.table);
    await waitUntilCount(nEREntityComponentsPage.records, beforeRecordsCount + 1);
    expect(await nEREntityComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await nEREntityComponentsPage.deleteNEREntity();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(nEREntityComponentsPage.records, beforeRecordsCount);
      expect(await nEREntityComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(nEREntityComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
