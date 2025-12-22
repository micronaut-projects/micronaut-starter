$version = '4.10.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '329FE3ACC76191CB6C56C093409D18B83C94615A7098EDB76EDB9FE39FA55874'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
