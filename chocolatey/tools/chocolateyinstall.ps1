$version = '3.1.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D43F51C8068CF318EB332A7871AFAA49217DD137A2FA8F9AD349E57680608EA0'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
