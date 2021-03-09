$version = '2.4.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AD6CF4E7118FBEEFB7DF4ECC7E8AD0352C9E06F77C656A0DCA7A1C53BFAC5AAD'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
