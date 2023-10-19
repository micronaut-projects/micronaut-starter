$version = '4.1.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '0CF4BDAA69C059EC0D55273390D331163E48DB2F8E97297D39672C7D917E6A81'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
