package br.fbv.cryptosvault.view;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.core.FileAdapter;
import br.fbv.cryptosvault.core.FileItem;
import br.fbv.cryptosvault.core.SafeFileAdapter;
import br.fbv.cryptosvault.model.account.AccountManager;
import br.fbv.cryptosvault.model.security.FileEncrypter;
import br.fbv.cryptosvault.model.util.Util;

/**
 * Project: Cryptos Vault Class: FilesActivity REVISION HISTORY Date          Developer       							  Comment ----------    -------------------------------------------- ---------------------------------------------- 12/11/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>     Initial Draft ----------    -------------------------------------------- ----------------------------------------------
 */
public class FilesActivity extends Activity implements OnCheckedChangeListener, OnClickListener, OnItemClickListener {

	private static final File ROOT_DIR = Environment.getExternalStorageDirectory();
	private static final File SAFE_DIR = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.safe");
	private static final String ENCRYPTER_PASS = "010203";
	private static final int SDCARD_NOT_READY = 0;
	private static final int RESET = 1;
	private static final int CLOSE = 2;

	private int checkableFileListSize;

	private TextView currentDirLabel1;
	private TextView currentDirLabel2;
	private ListView sdcardListView;
	private ListView safeListView;
	private CheckBox selectAllFiles;
	private CheckBox selectAllSafeFiles;
	private ImageButton addToVault;
	private ImageButton removeFromVault;

	private List<File> encryptList;
	private List<File> decryptList;

	/**
	 * @uml.property  name="fileAdapter"
	 * @uml.associationEnd  
	 */
	private FileAdapter fileAdapter;
	/**
	 * @uml.property  name="safeFileAdapter"
	 * @uml.associationEnd  
	 */
	private SafeFileAdapter safeFileAdapter;

	/**
	 * @uml.property  name="encrypterTool"
	 * @uml.associationEnd  
	 */
	private FileEncrypter encrypterTool;

	private File currentDir;

	private ProgressDialog progressDialog;

	private boolean backPressed;

	protected boolean resetPressed;

	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_screen);
		
		sdcardListView = (ListView) findViewById(R.id.sdcardListView);
		sdcardListView.setOnItemClickListener(this);
        safeListView = (ListView) findViewById(R.id.safeListView);

        selectAllFiles = (CheckBox) findViewById(R.id.selectAllFiles);
        selectAllFiles.setOnCheckedChangeListener(this);

        selectAllSafeFiles = (CheckBox) findViewById(R.id.selectAllSafeFiles);
        selectAllSafeFiles.setOnCheckedChangeListener(this);
        
        currentDirLabel1 = (TextView) findViewById(R.id.current_directory1);
        currentDirLabel2 = (TextView) findViewById(R.id.current_directory2);
        
        addToVault = (ImageButton) findViewById(R.id.addToVault);
        removeFromVault = (ImageButton) findViewById(R.id.RemoveFromVault);
        
        addToVault.setOnClickListener(this);
        removeFromVault.setOnClickListener(this);
        
        fileAdapter = new FileAdapter(this, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FileItem item = (FileItem) buttonView.getTag();
                item.setSelected(isChecked);
                if (isChecked) {
                    addToEncryptList(item.getFile());
                } else {
                    removeFromEncryptList(item.getFile());
                }
            }
        });
        safeFileAdapter = new SafeFileAdapter(this, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FileItem item = (FileItem) buttonView.getTag();
                item.setSelected(isChecked);
                if (isChecked) {
                    addToDecryptList(item.getFile());
                } else {
                    removeFromDecryptList(item.getFile());
                }
            }
        });

        
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
           fillFileList(ROOT_DIR);
            if (!SAFE_DIR.exists()) {
                SAFE_DIR.mkdir();
            }
            fillSafeFileList(SAFE_DIR);
        } else {
            showDialog(SDCARD_NOT_READY);
        }

        encrypterTool = FileEncrypter.getInstance(ENCRYPTER_PASS, this);

        encryptList = new ArrayList<File>();
        decryptList = new ArrayList<File>();
    }
    

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case SDCARD_NOT_READY:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.sdcardNotReady)).setPositiveButton(getString(R.string.ok_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                return builder.create();
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESET:
            	this.resetPressed = false;
                if (resultCode == RESULT_OK) {
                    doReset();
                }
                break;
            case CLOSE:
            	this.backPressed = false;
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
    	
		if (toast != null) {
			toast.cancel();
		}
		
		if (!this.backPressed) {
			this.backPressed = true;
			setResult(CLOSE);
			finish();
		}
    }
    
    private void fillFileList(File dir) {

       currentDir = dir;

       currentDirLabel1.setText(dir.getAbsolutePath());
       currentDirLabel2.setText(dir.getAbsolutePath());

        // List directory content.
        File[] dirFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isHidden()) {
                    return false;
                }
                return true;
            }
        });
        // Separates dirs e files.
        List<FileItem> items = new ArrayList<FileItem>();
        List<FileItem> files = new ArrayList<FileItem>();
        if (dirFiles != null) {
            for (File ff : dirFiles) {
                if (ff.isDirectory()) {
                    items.add(new FileItem(ff, false));
                } else {
                    files.add(new FileItem(ff, false));
                }
            }
        }
        checkableFileListSize = files.size();
        // Sort the two lists.
        Collections.sort(items);
        Collections.sort(files);
        items.addAll(files);
        // Inserts "back to parent" item if necessary.
        if (!dir.getAbsolutePath().equals(ROOT_DIR.getAbsolutePath())) {
            items.add(0, new FileItem(dir.getParentFile(), true));
        }

        fileAdapter.setItems(items);
        sdcardListView.setAdapter(fileAdapter);

        selectAllFiles.setChecked(false);
        selectAllFiles.setEnabled(checkableFileListSize > 0);
    }

    private void fillSafeFileList(File dir) {

        // List directory content.
        File[] dirFiles = dir.listFiles();
        // Separates dirs e files.
        List<FileItem> files = new ArrayList<FileItem>();
        if (dirFiles != null) {
            for (File ff : dirFiles) {
                files.add(new FileItem(ff, false));
            }
        }
        // Sort the list.
        Collections.sort(files);

        safeFileAdapter.setItems(files);
        safeListView.setAdapter(safeFileAdapter);

        selectAllSafeFiles.setChecked(false);
        selectAllSafeFiles.setEnabled(safeFileAdapter.getItems().size() > 0);
    }

    private void addToEncryptList(File file) {
        if (!encryptList.contains(file)) {
            encryptList.add(file);
        }
        if (encryptList.size() == checkableFileListSize) {
            selectAllFiles.setChecked(true);
        }
    }

    private void removeFromEncryptList(File file) {
        if (encryptList.contains(file)) {
            encryptList.remove(file);
        }
        if (encryptList.size() < checkableFileListSize && selectAllFiles.isChecked()) {
            selectAllFiles.setChecked(false);
        }
    }

    private void addToDecryptList(File file) {
        if (!decryptList.contains(file)) {
            decryptList.add(file);
        }
        if (decryptList.size() == safeFileAdapter.getItems().size()) {
            selectAllSafeFiles.setChecked(true);
        }
    }

    private void removeFromDecryptList(File file) {
        if (decryptList.contains(file)) {
            decryptList.remove(file);
        }
        if (decryptList.size() < safeFileAdapter.getItems().size() && selectAllSafeFiles.isChecked()) {
            selectAllSafeFiles.setChecked(false);
        }
    }

    private void startBackgroundThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.NORM_PRIORITY - 1);
        thread.start();
    }

	private Handler dismissCryptationProgressDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			fillFileList(currentDir);
			fillSafeFileList(SAFE_DIR);
			progressDialog.dismiss();
		};
	};

	private Runnable encryptationRunnable = new Runnable() {
		@Override
		public void run() {
			for (File file : encryptList) {

				String fileName = file.getName();
				fileName = encrypterTool.getEncryptFileName(fileName);
				fileName = SAFE_DIR.getAbsolutePath() + File.separatorChar
						+ fileName;
				File fileOutput = new File(fileName);

				try {
					FileInputStream inputStream = new FileInputStream(file);
					FileOutputStream outputStream = new FileOutputStream(
							fileOutput);
					encrypterTool.encrypt(inputStream, outputStream);
					file.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}

			encryptList.clear();

			dismissCryptationProgressDialogHandler.sendEmptyMessage(0);
		}
	};

    private void doEncryptation() {

        if (encryptList.size() > 0) {
            doProgress();
            startBackgroundThread(encryptationRunnable);
        } else {
        	showToast(getResources().getString(R.string.selectSdcardFile));
        }
    }

    private void doProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.label_wait), true, false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
                    return true;
                }
                return false;
            }
        });
    }

    private class DecryptationRunnable implements Runnable {

        private Handler finishHandler;

        public DecryptationRunnable(Handler finishHandler) {
            this.finishHandler = finishHandler;
        }

        @Override
        public void run() {
            for (File file : decryptList) {

                String fileName = file.getName();
                fileName = encrypterTool.getDecryptFileName(fileName);
                fileName = currentDir.getAbsolutePath() + File.separatorChar + fileName;
                File fileOutput = new File(fileName);

                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    FileOutputStream outputStream = new FileOutputStream(fileOutput);
                    encrypterTool.decrypt(inputStream, outputStream);
                    file.delete();

                    encrypterTool.removeReference(file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            encryptList.clear();

            finishHandler.sendEmptyMessage(0);
        }
    };

    private void doDecryptation() {

        if (decryptList.size() > 0) {
            doProgress();
            startBackgroundThread(new DecryptationRunnable(dismissCryptationProgressDialogHandler));
        } else {
        	showToast(getResources().getString(R.string.label_select_safe_file));
        }
    }

    private Handler dismissResetProgressDialogHandler = new Handler() {
                                                          public void handleMessage(Message msg) {
                                                              progressDialog.dismiss();
                                                              AccountManager.getInstance(FilesActivity.this).reset();
                                                              Intent intent = new Intent(FilesActivity.this, RegisterActivity.class);
                                                              startActivity(intent);
                                                              finish();
                                                          };
                                                      };

    private void doReset() {
        decryptList.clear();
        for (FileItem item : safeFileAdapter.getItems()) {
            decryptList.add(item.getFile());
        }
        progressDialog = ProgressDialog.show(this, "", getString(R.string.label_wait), true, false);
        startBackgroundThread(new DecryptationRunnable(dismissResetProgressDialogHandler));
    }
    
    public void showToast(String text){
    	if (toast == null){
    		toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
    	} else {
        	toast.cancel();
    		toast.setText(text);
    	}
    	toast.show();
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileItem item = (FileItem) parent.getItemAtPosition(position);
        if (item.getFile().isDirectory() || item.isBackToParent()) {
            fillFileList(item.getFile());
        }
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.addToVault) {
			doEncryptation();
		}
		else if(v.getId() == R.id.RemoveFromVault){
			doDecryptation();
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.selectAllFiles) {
			List<FileItem> files = fileAdapter.getItems();
			if (isChecked) {
				for (FileItem fileItem : files) {
					if (!fileItem.isSelected() && fileItem.getFile().isFile()) {
						fileItem.setSelected(true);
						addToEncryptList(fileItem.getFile());
					}
				}
			} else {
				for (FileItem fileItem : files) {
					if (fileItem.isSelected()) {
						fileItem.setSelected(false);
						removeFromEncryptList(fileItem.getFile());
					}
				}
			}
			sdcardListView.setAdapter(fileAdapter);
		} else if (buttonView.getId() == R.id.selectAllSafeFiles) {
			List<FileItem> files = safeFileAdapter.getItems();
			for (FileItem fileItem : files) {
				if (isChecked) {
					if (!fileItem.isSelected() && fileItem.getFile().isFile()) {
						fileItem.setSelected(true);
						addToDecryptList(fileItem.getFile());
					}
				} else {
					if (fileItem.isSelected()) {
						fileItem.setSelected(false);
						removeFromDecryptList(fileItem.getFile());
					}
				}
			}
			safeListView.setAdapter(safeFileAdapter);
		} 
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == R.id.about_menu_item) {
			intent = new Intent(this, AboutActivity.class);
		}
		else if (item.getItemId() == R.id.config_menu_item){
			intent = new Intent(this, ConfigurationActivity.class);
		}
		else if (item.getItemId() == R.id.exit_menu_item){
			Util.stopService(this);
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

}