import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TranscriptsComponentsPage from './transcripts.page-object';
import TranscriptsUpdatePage from './transcripts-update.page-object';
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

describe('Transcripts e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let transcriptsComponentsPage: TranscriptsComponentsPage;
  let transcriptsUpdatePage: TranscriptsUpdatePage;
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
    transcriptsComponentsPage = new TranscriptsComponentsPage();
    transcriptsComponentsPage = await transcriptsComponentsPage.goToPage(navBarPage);
  });

  it('should load Transcripts', async () => {
    expect(await transcriptsComponentsPage.title.getText()).to.match(/Transcripts/);
    expect(await transcriptsComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Transcripts', async () => {
    const beforeRecordsCount = (await isVisible(transcriptsComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(transcriptsComponentsPage.table);
    transcriptsUpdatePage = await transcriptsComponentsPage.goToCreateTranscripts();
    await transcriptsUpdatePage.enterData();
    expect(await isVisible(transcriptsUpdatePage.saveButton)).to.be.false;

    expect(await transcriptsComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(transcriptsComponentsPage.table);
    await waitUntilCount(transcriptsComponentsPage.records, beforeRecordsCount + 1);
    expect(await transcriptsComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await transcriptsComponentsPage.deleteTranscripts();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(transcriptsComponentsPage.records, beforeRecordsCount);
      expect(await transcriptsComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(transcriptsComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
